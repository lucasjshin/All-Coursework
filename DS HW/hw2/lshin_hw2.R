#### Homework #2 ####
# Lucas Shin
# Intro to Data Science
# October 30, 2020

library(tidyverse)
library(ggplot2)
library(babynames)
library(ggplot2)
View(babynames)

# 1
babynames %>%
   group_by(name) %>% 
   summarize(total = sum(n)) %>% 
   arrange(-total) %>% 
   head(10)


# 2
babynames %>% 
   select(year, n, prop) %>% 
   filter(year >= 1880) %>% 
   group_by(year) %>% 
   ggplot(mapping = aes(x = year,
                        y = n)) +
   geom_bar(stat = "identity") +
   labs(y = "Total US Births")
?babynames

# 3
#The US births in 2000 was approximately 4,059,000 # according to https://pubmed.ncbi.nlm.nih.gov/11876093/

babynames %>%
   group_by(year) %>% 
   filter(year == 2000) %>%
   summarize(total = sum(n))
# The number of US births reported by the government in 2000 is about 300,000 greater than that reported by the SSA.
# I don't think these values should be equal. According to the description of babynames, only names with at least 5 
# uses were reported. Thus, if an uncommon name was given to a baby, it was not included in this data set, so the 
# total number of births should be lower than the total number reported by the government. 


# 4
babynames %>% 
   group_by(name) %>% 
   summarize(total = sum(n)) %>% 
   filter(total>=1000) %>% 
   mutate(numChar = str_length(name)) %>% 
   arrange(-numChar)
# Maryelizabeth and Michaelangelo (each 13 letters long)


# 5
babynames %>% 
   mutate(count = str_length(name)) %>%
   mutate(chars = n*count) %>%
   group_by(year) %>%
   summarize(lengthOfNames = sum(chars)/sum(n)) %>% 
   ggplot(mapping = aes(x = year,
                        y = lengthOfNames)) +
   geom_line() +
   labs(x = "Year",
        y = "Avg Length of Name") 

   
# 6
names <- babynames %>% 
   filter(year %in% c("1880", "2017")) %>% 
   select(year, sex, name, n) %>% 
   group_by(name, year) %>% 
   mutate(total = sum(n)) %>% 
   mutate(propO = n/total)%>% 
   filter(sex == "F") %>% 
   group_by(name) %>% 
   summarize(change = max(propO) - min(propO)) %>% 
   filter(change > 0, 
          change < 1) %>% 
   arrange(-change) %>% 
   head(3)

name3 <- babynames %>% 
   group_by(year, name) %>% 
   filter(name %in% c("Donnie", "Leslie", "Robbie")) %>% 
   select(year, sex, name, n) %>% 
   mutate(total = sum(n)) %>% 
   mutate(prop = n/total) %>%
   filter(sex == "F") 

name3 %>% 
   ggplot(mapping = aes(x = year,
                     y = prop)) +
   geom_line(aes(color = name)) +
   geom_text(data = name3 %>% filter(year == "1981"),
             aes(label = name),
             vjust = 2) +
   xlab("Year") +
   ylab("Proportion of Females") +
   theme_bw() +
   labs(title = "Change in the 'Femaleness' of Names Over Time (1880-2017)")
?labs()
