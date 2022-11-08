# Biometrics HW #4
# Lucas Shin

library(tidyverse)
library(ptw)
library(ggplot2)
library("png")

imageList <- list.files("/Users/lucasshin/Desktop/Biometrics/Biometrics HW4/trainingfaces2/")

photo <- imageList[1]

image1 = readPNG(paste("/Users/lucasshin/Desktop/Biometrics/Biometrics HW4/trainingfaces2/",
                      photo, sep = ""))

image2 <- padzeros(image1,1,side="both") # Add zeros to the left and right side of the matrix (both)
image3 <- padzeros(t(image2),1,side="both") # Rotates the matrix and adds zeros to both sides
padded <- t(image3) # Rotates the matrix back so that it is in the same orientation as the orig

print(image2) # displays the original matrix
print(padded) # displays the zero-padded matrix

fMatrix <- matrix(0L, nrow = nrow(padded)-2, ncol = ncol(padded)-2) # create empty matrix

for (i in 1:(nrow(padded)-2)) {
   print(i)
   i = i + 1
   print(i)
   for (j in 1:(ncol(padded)-2)) {
      j = j + 1
      sum = 0
      #print(i)
      #print(j)
      if (padded[i-1,j-1] >= padded[i,j]) {
         sum <- sum + 1
      } 
      
      if (padded[i-1,j] >= padded[i,j]) {
         sum <- sum + 2
      } 
      
      if (padded[i-1,j+1] >= padded[i,j]) {
         sum <- sum + 4
      } 
      
      if (padded[i,j+1] >= padded[i,j]) {
         sum <- sum + 8
      } 
      
      if (padded[i+1,j+1] >= padded[i,j]) {
         sum <- sum + 16
      } 
      
      if (padded[i+1,j] >= padded[i,j]) {
         sum <- sum + 32
      } 
      
      if (padded[i+1,j-1] >= padded[i,j]) {
         sum <- sum + 64
      } 
      
      if (padded[i,j-1] >= padded[i,j]) {
         sum <- sum + 128
      }
      fMatrix[i-1,j-1] <- sum
   }
}

# declare sets of data for histograms
set1 <- as.vector(fMatrix[1:15,1:15])
set2 <- as.vector(fMatrix[1:15,16:30])
set3 <- as.vector(fMatrix[1:15,31:45])
set4 <- as.vector(fMatrix[1:15,46:60])
set5 <- as.vector(fMatrix[16:30,1:15])
set6 <- as.vector(fMatrix[16:30,16:30])
set7 <- as.vector(fMatrix[16:30,31:45])
set8 <- as.vector(fMatrix[16:30,31:45])
set9 <- as.vector(fMatrix[16:30,46:60])
set10 <- as.vector(fMatrix[31:45,16:30])
set11 <- as.vector(fMatrix[31:45,31:45])
set12 <- as.vector(fMatrix[31:45,46:60])
set13 <- as.vector(fMatrix[46:60,1:15])
set14 <- as.vector(fMatrix[46:60,16:30])
set15 <- as.vector(fMatrix[46:60,31:45])
set16 <- as.vector(fMatrix[46:60,46:60])

allFvectors <- c()

allSets <- list(set1, set2, set3, set4, set5, set6, set7, set8, set9, set10, set11, set12, set13, set14, set15, set16)

for (k in 1:length(allSets)) {
   relevant <- allSets[[k]]
   bin1 <- 0
   bin2 <- 0
   bin3 <- 0
   bin4 <- 0
   bin5 <- 0
   bin6 <- 0
   bin7 <- 0
   bin8 <- 0
   for (m in 1:225) {
      pixel <- relevant[m]
      #print(pixel)
      if ((pixel >= 0) & (pixel < 32)) {
         bin1 <- bin1 + 1
      }
      if ((pixel >= 32) & (pixel < 64)) {
         bin2 <- bin2 + 1
      }
      if ((pixel >= 64) & (pixel < 96)) {
         bin3 <- bin3 + 1
      }
      if ((pixel >= 96) & (pixel < 128)) {
         bin4 <- bin4 + 1
      }
      if ((pixel >= 128) & (pixel < 160)) {
         bin5 <- bin5 + 1
      }
      if ((pixel >= 160) & (pixel < 192)) {
         bin6 <- bin6 + 1
      }
      if ((pixel >= 192) & (pixel < 224)) {
         bin7 <- bin7 + 1
      }
      if ((pixel >= 224) & (pixel < 256)) {
         bin8 <- bin8 + 1
      }
   }
   fVector <- c(bin1, bin2, bin3, bin4, bin5, 
                        bin6, bin7, bin8)
   allFvectors <- c(allFvectors, fVector)
}
