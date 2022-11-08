# Lucas Shin
# Biometrics, HW3

library(tidyverse)
library(ggplot2)
library("png")

# Step 1
faces <- matrix(nrow = 3600, 
                ncol = 0)

imageList <- list.files("/Users/lucasshin/Desktop/Biometrics/Biometrics HW 3/testingfaces2/")

for (i in 1:length(imageList)) {
   photo = imageList[i]
   image = readPNG(paste("/Users/lucasshin/Desktop/Biometrics/Biometrics HW 3/testingfaces2/",
                         photo, sep = ""))
   vecImage = as.vector(image)
   faces <- cbind(faces, vecImage)
}

rowMeans <- rowMeans(faces)

subjMeans <- matrix(nrow = 3600, ncol = 0)
for (j in 1:26) {
   imagePerSubject <- matrix(nrow = 3600, ncol = 0)
   for (k in 1:length(imageList)) {
      if (as.numeric(substr(c(imageList[k]), start = 2, stop = 3)) == j) {
         imagePerSubject = cbind(imagePerSubject, faces[ , k])
      }
   }
   subjMeans = cbind(subjMeans, rowMeans(imagePerSubject))
}

colnames(subjMeans) <- c(1:26) # renames columns

subjMeans<-subjMeans[,-c(14,21)] # deletes columns 14 and 21, which are NA (since there are no subjects 14 and 21 in the set)

subjects <- colnames(subjMeans)
subjects <- as.numeric(subjects)


# Step 2
sw <- matrix(0L, nrow=3600, ncol=3600)
for (m in 1:length(subjects)) {
   subNum = subjects[m]
   for (n in 1:length(imageList)) {
      if (as.numeric(substr(c(imageList[n]), start = 2, stop = 3)) == subNum) {
         var <- faces[,n] - subjMeans[subNum]
         sw <- sw+(var%*%t(var))
      }
   }
}

sb <- matrix(0L, nrow=3600, ncol=3600)
for (p in 1:length(subjects)) {
   sNum = subjects[p]
   for (q in 1:length(imageList)) {
      nphotos <- 0
      if (as.numeric(substr(c(imageList[q]), start = 2, stop = 3)) == sNum) {
         nphotos = nphotos+1
      }
   }
   variable <- subjMeans[sNum] - rowMeans
   sb <- sb + nphotos*(variable%*%t(variable))
}


# Step 3
e <- eigen(solve(sw)%*%sb)
eVectors <- e$vectors
eValues <- e$values


# Step 4
# to find weighted sum using all eigenvectors
inputImage <- faces[,1]
weight1 <- abs(t(eVectors) %*% inputImage)
weight1 <- colSums(weight1)

# to find weighted sum using first 10 eigenvectors
eSubset <- eVectors[,1:10]
weight2 <- abs(t(eSubset) %*% inputImage)
weight2 <- colSums(weight1)
