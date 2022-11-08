library(tidyverse)
library(rvest)


nbaSalaries <- read_csv("nbaSalaries.csv")
advanced1920 <- read_csv("2019.2020advancedStats.csv")
perGame1920 <- read_csv("2019.2020perGameStats.csv")

statsPlayers <- c()

for (i in 1:nrow(perGame1920)) {
   statsPlayers <- c(statsPlayers, perGame1920$Player[i])
}

salaryPlayers <- c()
for (j in 1:nrow(nbaSalaries)) {
   salaryPlayers <- c(salaryPlayers, perGame1920$Player[j])
}

nbaSalariesFiltered <- nbaSalaries %>% 
   filter(Player %in% statsPlayers) %>% 
   select(Player, `2019-20`, `2020-21`, `2021-22`, `2022-23`, `2023-24`, `2024-25`, Guaranteed)

# remove punctuation from entries in Guaranteed column
nbaSalariesFiltered$Guaranteed <- str_remove(nbaSalariesFiltered$Guaranteed,
                                             "[:punct:]")

