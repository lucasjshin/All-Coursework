# Biometrics HW 5
# Lucas Shin

library(tidyverse)
library(ptw)
library(ggplot2)
library("png")
library(stringr)
library("reshape")
library(ptw)

#Input: Matrix Output: Matrix with a border of zeros, also known as zero padding
#Create a function that takes in an 3x3 matrix and returns a vector a units long Values equal to and above the middle element are given a score of 1 and those below are given a zero.
padding <- function(src) {
   mat1 <- padzeros(src,1,side="both") 
   mat2 <- padzeros(t(mat1),1,side="both") 
   return(t(mat2))
}

lbpthreshold <- function(mat){
   # Extracts the middle element of the 3 by 3 matrix
   val <- mat[2,2]
   tmp <- mat
   tmp[mat >= val] <- 1 
   tmp[mat < val] <- 0
   dst <- c(tmp[1,1], tmp[1,2], tmp[1,3], tmp[2,3], tmp[3,3], tmp[3,2], tmp[3,1], tmp[2,1])
   # Removes the middle element of the  3x3 vector
   return(dst) 
}

#Convert a binary number into a decimal value. Binary number given in reverse order. (e.g. 1100 is 3, not 12)
#Input: Image matrix Output: LBP matrix
lbpval <- function(x){ 
   val <- 0
   for (i in 1:length(x)){
      val <- val + x[i] * 2 ^ (i-1)
   }
   return(val) 
}

# Creates a new matrix full of zeros, smaller in size to remove the zero padding
lbpmat <- function(src){
   dst <- matrix(0L, nrow = (dim(src)[1] - 2), ncol = (dim(src)[2] - 2))
   rows <- dim(src)[1] 
   cols <- dim(src)[2]
   for (row in c(2:(rows-1))){ 
      for(col in c(2:(cols-1))){
         tmp <- src[c( (row-1):(row+1)), c( (col-1):(col+1))] 
         threshvec <- lbpthreshold(tmp)
         binnum <- lbpval(threshvec)
         dst[row-1,col-1] <- binnum
   } }
   return(dst) 
}

#Divide image into windows and create histogram feature vectors
lbphist <- function(src){
   bins <- seq(0,256,l=9)
   h <- hist(as.vector(src), breaks = bins,plot=FALSE) 
   return(h$counts)
}
lbpfeatvec <- function(src, numywin, numxwin){ 
   rows <- dim(src)[1]
   cols <- dim(src)[2]
   feats = c()
   yrange <- floor(rows / numywin) - 1 
   xrange <- floor(cols / numxwin) - 1
   for(y in c(1:numywin)){ 
      for(x in c(1:numxwin)){
         ymin <- y + (y-1)*yrange
         ymax <- y + (y-1)*yrange + yrange 
         xmin <- x + (x-1)*xrange
         xmax <- x + (x-1)*xrange + xrange
         tmp <- src[c(ymin:ymax),c(xmin:xmax)] 
         featvec <- lbphist(tmp)
         feats <- c(feats, featvec)
      } 
   }
   return(feats) 
}

# Compare to LBP feature vectors
lbpmatcher <- function(l1, l2){ 
   return (sum(abs(l1 - l2)))
}

# MAIN FUNCTION/CONTROL SEQUENCE
imgdir <- "/Users/lucasshin/Desktop/Biometrics/Biometrics HW 5/fa20cropped/"
img_filenames <- list.files(path=imgdir, pattern="*.png")

# Stores all of the LBP feature vectors to compare two images
lbpimg <- vector("list", length(img_filenames)) 
lbpvecs <- vector("list", length(img_filenames))

# Creates 16 windows from image
nrows <- 4 
ncols <- 4
for (i in 1:length(img_filenames)) {
   imgdata <- readPNG(paste(imgdir, img_filenames[i], sep =""))
   imgdata <- (imgdata - min(imgdata)) / (max(imgdata) - min(imgdata))
   paddedimg <- padding(imgdata)
   lbpimg[[i]] <- lbpmat(paddedimg)
   lbpvecs[[i]] <- lbpfeatvec(lbpimg[[i]], nrows, ncols)
}

# Image matching experiment
# Create a matrix to hold scores
scores = matrix(0L, nrow = length(img_filenames), ncol = length(img_filenames)) 
for (i in 1:length(img_filenames)) {
   for (j in 1:length(img_filenames)) {
      scores[i,j] <- lbpmatcher(lbpvecs[[i]], lbpvecs[[j]])
   } 
}

# Directory of Training Data
trainingdir <- "/Users/lucasshin/Desktop/Biometrics/Biometrics HW 5/testingfaces2/"
training_filenames <- list.files(path=trainingdir, pattern="*.png")
trainingdata <- vector("list", length(training_filenames))
for (i in 1:length(training_filenames)) {
   trainingdata[[i]] <- readPNG(paste(trainingdir, training_filenames[i], sep =""))
   trainingdata[[i]] <- (trainingdata[[i]] - min(trainingdata[[i]])) / (max(trainingdata[[i]]) - min(trainingdata[[i]]))
}

# Step 1. Implement each image as a d-dimensional column vector x1, x2,. . . ,xN and compute the average μ.
trainingmatrix = c()
for (i in 1:length(training_filenames)){
   trainingmatrix = cbind(trainingmatrix, as.vector(t(trainingdata[[i]]))) 
}

u = rowMeans(trainingmatrix)

# Step 2. Define the matrix X as the follows: X = [(x1-μ) (x2-μ) . . . (xN-μ)]. X is a d x N matrix.
normmatrix <- apply(trainingmatrix, 2, function(x) x - u)

# Step 3. Calculate the covariance matrix C = XXT, where XT is the transpose of X. d by N multiplied by a N by d matrix is results in a d by d matrix.
covarmtx = normmatrix %*% t(normmatrix)

# Step 4. Compute the Eigenvectors of the covariance matrix C by solving the following
ev = eigen(covarmtx)
pc = ev$vectors[,c(1:15)]

# Directory of Testing Data
testingdir <- "/Users/lucasshin/Desktop/Biometrics/Biometrics HW 5/fa20cropped/"
testing_filenames <- list.files(path=testingdir, pattern="*.png")
testingdata <- vector("list", length(testing_filenames))
for (i in 1:length(testing_filenames)) {
   testingdata[[i]] <- readPNG(paste(testingdir, testing_filenames[i], sep =""))
   #testingdata[[i]] <- (testingdata[[i]] - min(testingdata[[i]])) / (max(testingdata[[i]]) - min(testingdata[[i]]))
}

testingmatrix = c()
for (i in 1:length(testing_filenames)){
   testingmatrix = cbind(testingmatrix, as.vector(t(testingdata[[i]]))) 
}

weights <- vector("list", length(testing_filenames))
# make vector of all the weights of the images
for (img in 1:length(testing_filenames)) {
   img_data <- testingmatrix[,img]
   weight <- abs(t(pc) %*% img_data)
   weights[[img]] <- weight
}

# Image matching experiment
# Create a matrix to hold scores
scores1 = matrix(0L, nrow = length(testing_filenames), ncol = length(testing_filenames)) 
for (i in 1:length(testing_filenames)) {
   for (j in 1:length(testing_filenames)) {
      scores1[i,j] <- lbpmatcher(weights[[i]], weights[[j]])
   } 
}

# normalize scores for scores and scores1
LBPmax <- max(scores)
LBPmin <- min(scores)
PCAmax <- max(scores1)
PCAmin <- min(scores1)

LBPrange <- LBPmax-LBPmin
PCArange <- PCAmax-PCAmin

for (i in 1:length(img_filenames)) {
   for (j in 1:length(img_filenames)) {
      scores[i,j] <- (scores[i,j] - LBPmin)/LBPrange
   } 
}
for (i in 1:length(testing_filenames)) {
   for (j in 1:length(testing_filenames)) {
      scores1[i,j] <- (scores1[i,j] - PCAmin)/PCArange
   } 
}


# make min score matrix
scoresMin = matrix(0L, nrow = length(testing_filenames), ncol = length(testing_filenames)) 
for (i in 1:length(testing_filenames)) {
   for (j in 1:length(testing_filenames)) {
      scoresMin[i,j] <- min(scores[i,j], scores1[i,j])
   }
}

# make max score matrix
scoresMax = matrix(0L, nrow = length(testing_filenames), ncol = length(testing_filenames)) 
for (i in 1:length(testing_filenames)) {
   for (j in 1:length(testing_filenames)) {
      scoresMax[i,j] <- max(scores[i,j], scores1[i,j])
   }
}

# make sum score matrix
scoresSum = matrix(0L, nrow = length(testing_filenames), ncol = length(testing_filenames)) 
for (i in 1:length(testing_filenames)) {
   for (j in 1:length(testing_filenames)) {
         scoresSum[i,j] <- scores[i,j] + scores1[i,j]
   } 
}


#### ROC CURVES ####

## for LBP ##
d <- scores
gallery <- c()
query <- c()

for (i in 1:length(img_filenames)) {
   gallery <- c(gallery, substr(img_filenames[i], start = 2, stop = 3))
}

gallery <- as.matrix(gallery)
query <- gallery

colnames(d) <- as.matrix(gallery) 
rownames(d) <- as.matrix(query)
m <- melt(d)
names(m) <- c("xA", "xE", "dist")

# Compare the labels of xA and xE to see which ones are different and which ones are the same.
genuine <- m[m[,1] == m[,2],] 
impostor <- m[m[,1] != m[,2],]

# Calculate the density of each distribution
genden <- density(genuine[,3]) 
impden <- density(impostor[,3])
xmin <- min(genden$x,impden$x) 
xmax <- max(genden$x,impden$x) 
ymin <- min(genden$y,impden$y) 
ymax <- max(genden$y,impden$y)

plot(genden, xlim=c(xmin,xmax), ylim=c(ymin,ymax), main="Distribution of Genuine and Impostor Scores", xlab="Scores", ylab="Density", col = "blue", lwd = 2)
lines(impden, col = "red", lwd = 2)
polygon(genden, col=rgb(0,0,1,0.5))
polygon(impden, col=rgb(1,0,0,0.5))
legend('topright', c("Genuine","Impostor"), col = c("blue","red"), lty = c(1,1), lwd = c(2.5,2.5))

# Calculate Sensitivity Index (D-Prime)
dprime = function(mu1,mu0,sig1,sig0) { 
   sqrt(2) * abs(mu1 - mu0) / sqrt(sig1^2 + sig0^2) 
} 
mu1 = mean(genuine[,3])
mu0 = mean(impostor[,3])
sig1 = sd(genuine[,3])
sig0 = sd(impostor[,3])
print(paste("Sensitivity Index:",dprime(mu1,mu0,sig1,sig0)))

# Create a function to FAR and FRR given a threshold
# Distance scores are used to compute this DET curve. If the scores are # similarity scores, then reverse the logical operators
DET = function(threshold, gen, imp){
   # The percentage of impostor scores that are claimed to be genuine
   far = length(imp[ imp < threshold]) / length(imp)
   # The percentage of true identities that are claimed to be impostors 
   frr = length(gen[ gen > threshold]) / length(gen)
   datapoint = c(threshold,far,frr) 
   return(datapoint)
}

thresholds = seq(xmin, xmax,(xmax-xmin)/100)
detcurve1 = t(sapply(thresholds, DET, genuine[,3], impostor[,3])) 

colnames(detcurve1) = c("threshold","far","frr")

## for PCA ##
d <- scores1
gallery <- c()
query <- c()

for (i in 1:length(img_filenames)) {
   gallery <- c(gallery, substr(img_filenames[i], start = 2, stop = 3))
}

gallery <- as.matrix(gallery)
query <- gallery

colnames(d) <- as.matrix(gallery) 
rownames(d) <- as.matrix(query)
m <- melt(d)
names(m) <- c("xA", "xE", "dist")

# Compare the labels of xA and xE to see which ones are different and which ones are the same.
genuine <- m[m[,1] == m[,2],] 
impostor <- m[m[,1] != m[,2],]

# Calculate the density of each distribution
genden <- density(genuine[,3]) 
impden <- density(impostor[,3])
xmin <- min(genden$x,impden$x) 
xmax <- max(genden$x,impden$x) 
ymin <- min(genden$y,impden$y) 
ymax <- max(genden$y,impden$y)

plot(genden, xlim=c(xmin,xmax), ylim=c(ymin,ymax), main="Distribution of Genuine and Impostor Scores", xlab="Scores", ylab="Density", col = "blue", lwd = 2)
lines(impden, col = "red", lwd = 2)
polygon(genden, col=rgb(0,0,1,0.5))
polygon(impden, col=rgb(1,0,0,0.5))
legend('topright', c("Genuine","Impostor"), col = c("blue","red"), lty = c(1,1), lwd = c(2.5,2.5))

# Calculate Sensitivity Index (D-Prime)
dprime = function(mu1,mu0,sig1,sig0) { 
   sqrt(2) * abs(mu1 - mu0) / sqrt(sig1^2 + sig0^2) 
} 
mu1 = mean(genuine[,3])
mu0 = mean(impostor[,3])
sig1 = sd(genuine[,3])
sig0 = sd(impostor[,3])
print(paste("Sensitivity Index:",dprime(mu1,mu0,sig1,sig0)))

# Create a function to FAR and FRR given a threshold
# Distance scores are used to compute this DET curve. If the scores are # similarity scores, then reverse the logical operators
DET = function(threshold, gen, imp){
   # The percentage of impostor scores that are claimed to be genuine
   far = length(imp[ imp < threshold]) / length(imp)
   # The percentage of true identities that are claimed to be impostors 
   frr = length(gen[ gen > threshold]) / length(gen)
   datapoint = c(threshold,far,frr) 
   return(datapoint)
}

thresholds = seq(xmin, xmax,(xmax-xmin)/100)
detcurve2 = t(sapply(thresholds, DET, genuine[,3], impostor[,3])) 

colnames(detcurve2) = c("threshold","far","frr")


## for Min ##
d <- scoresMin
gallery <- c()
query <- c()

for (i in 1:length(img_filenames)) {
   gallery <- c(gallery, substr(img_filenames[i], start = 2, stop = 3))
}

gallery <- as.matrix(gallery)
query <- gallery

colnames(d) <- as.matrix(gallery) 
rownames(d) <- as.matrix(query)
m <- melt(d)
names(m) <- c("xA", "xE", "dist")

# Compare the labels of xA and xE to see which ones are different and which ones are the same.
genuine <- m[m[,1] == m[,2],] 
impostor <- m[m[,1] != m[,2],]

# Calculate the density of each distribution
genden <- density(genuine[,3]) 
impden <- density(impostor[,3])
xmin <- min(genden$x,impden$x) 
xmax <- max(genden$x,impden$x) 
ymin <- min(genden$y,impden$y) 
ymax <- max(genden$y,impden$y)

plot(genden, xlim=c(xmin,xmax), ylim=c(ymin,ymax), main="Distribution of Genuine and Impostor Scores", xlab="Scores", ylab="Density", col = "blue", lwd = 2)
lines(impden, col = "red", lwd = 2)
polygon(genden, col=rgb(0,0,1,0.5))
polygon(impden, col=rgb(1,0,0,0.5))
legend('topright', c("Genuine","Impostor"), col = c("blue","red"), lty = c(1,1), lwd = c(2.5,2.5))

# Calculate Sensitivity Index (D-Prime)
dprime = function(mu1,mu0,sig1,sig0) { 
   sqrt(2) * abs(mu1 - mu0) / sqrt(sig1^2 + sig0^2) 
} 
mu1 = mean(genuine[,3])
mu0 = mean(impostor[,3])
sig1 = sd(genuine[,3])
sig0 = sd(impostor[,3])
print(paste("Sensitivity Index:",dprime(mu1,mu0,sig1,sig0)))

# Create a function to FAR and FRR given a threshold
# Distance scores are used to compute this DET curve. If the scores are # similarity scores, then reverse the logical operators
DET = function(threshold, gen, imp){
   # The percentage of impostor scores that are claimed to be genuine
   far = length(imp[ imp < threshold]) / length(imp)
   # The percentage of true identities that are claimed to be impostors 
   frr = length(gen[ gen > threshold]) / length(gen)
   datapoint = c(threshold,far,frr) 
   return(datapoint)
}

thresholds = seq(xmin, xmax,(xmax-xmin)/100)
detcurve3 = t(sapply(thresholds, DET, genuine[,3], impostor[,3])) 

colnames(detcurve3) = c("threshold","far","frr")


## for max ##
d <- scoresMax
gallery <- c()
query <- c()

for (i in 1:length(img_filenames)) {
   gallery <- c(gallery, substr(img_filenames[i], start = 2, stop = 3))
}

gallery <- as.matrix(gallery)
query <- gallery

colnames(d) <- as.matrix(gallery) 
rownames(d) <- as.matrix(query)
m <- melt(d)
names(m) <- c("xA", "xE", "dist")

# Compare the labels of xA and xE to see which ones are different and which ones are the same.
genuine <- m[m[,1] == m[,2],] 
impostor <- m[m[,1] != m[,2],]

# Calculate the density of each distribution
genden <- density(genuine[,3]) 
impden <- density(impostor[,3])
xmin <- min(genden$x,impden$x) 
xmax <- max(genden$x,impden$x) 
ymin <- min(genden$y,impden$y) 
ymax <- max(genden$y,impden$y)

plot(genden, xlim=c(xmin,xmax), ylim=c(ymin,ymax), main="Distribution of Genuine and Impostor Scores", xlab="Scores", ylab="Density", col = "blue", lwd = 2)
lines(impden, col = "red", lwd = 2)
polygon(genden, col=rgb(0,0,1,0.5))
polygon(impden, col=rgb(1,0,0,0.5))
legend('topright', c("Genuine","Impostor"), col = c("blue","red"), lty = c(1,1), lwd = c(2.5,2.5))

# Calculate Sensitivity Index (D-Prime)
dprime = function(mu1,mu0,sig1,sig0) { 
   sqrt(2) * abs(mu1 - mu0) / sqrt(sig1^2 + sig0^2) 
} 
mu1 = mean(genuine[,3])
mu0 = mean(impostor[,3])
sig1 = sd(genuine[,3])
sig0 = sd(impostor[,3])
print(paste("Sensitivity Index:",dprime(mu1,mu0,sig1,sig0)))

# Create a function to FAR and FRR given a threshold
# Distance scores are used to compute this DET curve. If the scores are # similarity scores, then reverse the logical operators
DET = function(threshold, gen, imp){
   # The percentage of impostor scores that are claimed to be genuine
   far = length(imp[ imp < threshold]) / length(imp)
   # The percentage of true identities that are claimed to be impostors 
   frr = length(gen[ gen > threshold]) / length(gen)
   datapoint = c(threshold,far,frr) 
   return(datapoint)
}

thresholds = seq(xmin, xmax,(xmax-xmin)/100)
detcurve4 = t(sapply(thresholds, DET, genuine[,3], impostor[,3])) 

colnames(detcurve4) = c("threshold","far","frr")


## for Sum ##
d <- scoresSum
gallery <- c()
query <- c()

for (i in 1:length(img_filenames)) {
   gallery <- c(gallery, substr(img_filenames[i], start = 2, stop = 3))
}

gallery <- as.matrix(gallery)
query <- gallery

colnames(d) <- as.matrix(gallery) 
rownames(d) <- as.matrix(query)
m <- melt(d)
names(m) <- c("xA", "xE", "dist")

# Compare the labels of xA and xE to see which ones are different and which ones are the same.
genuine <- m[m[,1] == m[,2],] 
impostor <- m[m[,1] != m[,2],]

# Calculate the density of each distribution
genden <- density(genuine[,3]) 
impden <- density(impostor[,3])
xmin <- min(genden$x,impden$x) 
xmax <- max(genden$x,impden$x) 
ymin <- min(genden$y,impden$y) 
ymax <- max(genden$y,impden$y)

plot(genden, xlim=c(xmin,xmax), ylim=c(ymin,ymax), main="Distribution of Genuine and Impostor Scores", xlab="Scores", ylab="Density", col = "blue", lwd = 2)
lines(impden, col = "red", lwd = 2)
polygon(genden, col=rgb(0,0,1,0.5))
polygon(impden, col=rgb(1,0,0,0.5))
legend('topright', c("Genuine","Impostor"), col = c("blue","red"), lty = c(1,1), lwd = c(2.5,2.5))

# Calculate Sensitivity Index (D-Prime)
dprime = function(mu1,mu0,sig1,sig0) { 
   sqrt(2) * abs(mu1 - mu0) / sqrt(sig1^2 + sig0^2) 
} 
mu1 = mean(genuine[,3])
mu0 = mean(impostor[,3])
sig1 = sd(genuine[,3])
sig0 = sd(impostor[,3])
print(paste("Sensitivity Index:",dprime(mu1,mu0,sig1,sig0)))

# Create a function to FAR and FRR given a threshold
# Distance scores are used to compute this DET curve. If the scores are # similarity scores, then reverse the logical operators
DET = function(threshold, gen, imp){
   # The percentage of impostor scores that are claimed to be genuine
   far = length(imp[ imp < threshold]) / length(imp)
   # The percentage of true identities that are claimed to be impostors 
   frr = length(gen[ gen > threshold]) / length(gen)
   datapoint = c(threshold,far,frr) 
   return(datapoint)
}

thresholds = seq(xmin, xmax,(xmax-xmin)/100)
detcurve5 = t(sapply(thresholds, DET, genuine[,3], impostor[,3])) 

colnames(detcurve5) = c("threshold","far","frr")

plot(detcurve1[,2], detcurve1[,3], type = "l", lwd = 2, xlab = "FAR", ylab = "FRR", main="ROC Curves")
lines(detcurve2[,2], detcurve2[,3], col = "blue")
lines(detcurve3[,2], detcurve3[,3], col = "green")
lines(detcurve4[,2], detcurve4[,3], col = "red")
lines(detcurve5[,2], detcurve5[,3], col = "orange")
legend('topright', c("LBP","PCA", "Min", "Max", "Sum"), col = c("black","blue", "green", "red", "orange"), lty = 1, lwd = c(2.5,2.5))


