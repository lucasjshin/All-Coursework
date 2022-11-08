#### Homework #1 ####
# Lucas Shin
# Biometrics
# October 1, 2020

library(tidyverse)
library(ggplot2)
library(dplyr)

# 1
SampleScoreMatrix <- read_csv("Sample_DissimilarityScoreMatrix_Mode1.csv", col_names = FALSE)
SampleGalleryList <- read_csv("Sample_GalleryIDList.csv", col_names = FALSE)
SampleQueryList <- read_csv("Sample_QueryIDList.csv", col_names = FALSE)

RealScoreMatrix <- read_csv("BSS1_DissimilarityScoreMatrix_GEI.csv", col_names = FALSE)
RealGalleryList <- read_csv("BSS1_GalleryIDList.csv", col_names = FALSE)
RealQueryList <- read_csv("BSS1_QueryIDList.csv", col_names = FALSE)


impScores <- c() # creates array for imposter scores
genScores <- c() # creates array for genuine scores

# iterates through 2D matrix, checks if score is imposter or genuine, and adds to respective array
for(j in 1:nrow(SampleQueryList)) { # 1:300 to truncate from the set of queries
   for(i in 1:nrow(SampleGalleryList)) { 
      if(SampleGalleryList[i,1]==SampleQueryList[j,1]) {
         genScores <- c(genScores, SampleScoreMatrix[j,i])
      } else {
         impScores <- c(impScores, SampleScoreMatrix[j,i])
      }
   }
   print(j)
}

impFrame <- data.frame(impScores) # create data frame for imposter scores
impFrameNew <- as.data.frame(t(impFrame)) # transforms data into entries of column instead of entries of row

genFrame <- data.frame(genScores) # create data frame for genuine scores
genFrameNew <- as.data.frame(t(genFrame)) # transforms data into entries of column instead of entries of row

# plots the distance scores in a density graph
ggplot(mapping = aes(x = V1)) +
   geom_density(data = impFrameNew,
                color = "red",
                fill = "red",
                alpha = 0.3) +
   geom_density(data = genFrameNew,
                color = "green",
                fill = "green",
                alpha = 0.3) +
   labs(title = "Distribution of Genuine and Imposter Scores",
        x = "Distance Scores", 
        y = "Density")


# 2
impMean <- mean(impFrameNew$V1)
genMean <- mean(genFrameNew$V1)
impStandDev <- sd(impFrameNew$V1)
genStandDev <- sd(genFrameNew$V1)

dPrime <- ((sqrt(2))*abs(genMean-impMean))/sqrt((genStandDev*genStandDev)+(impStandDev*impStandDev))
print(dPrime)


# 3
thresholdList <- c()
maxGenScore <- max(genFrameNew)
maxImpScore <- max(impFrameNew)
maxScore <- c(maxImpScore,maxGenScore)
maxThreshold <- max(maxScore)
inc <- maxThreshold/20
current <- 0

while(current <= maxThreshold) {
   thresholdList <- c(thresholdList, current)
   current = current + inc
}

FRRList <- c()
FARList <- c()

for(threshold in thresholdList) {
   # for FRR
   genReject <- c()
   for(k in genScores) {
      if(k > threshold) {
         genReject <- c(genReject, k)
      }
   }
   FRR <- (length(genReject) / length(genScores))
   FRRList <- c(FRRList, FRR)
   
   # for FAR
   impAllowed <- c()
   for(l in impScores) {
      if(l <= threshold) {
         impAllowed <- c(impAllowed, l)
      }
   }
   FAR <- (length(impAllowed) / length(impScores))
   FARList <- c(FARList, FAR)
}
print(FARList)
print(FRRList)


ROCpoints <- data.frame(FARList, FRRList)

# to plot
ROCpoints %>% 
   ggplot(mapping = aes(x = FARList,
                        y = FRRList)) +
   geom_point() +
   geom_line() +
   theme_bw() +
   geom_abline()
