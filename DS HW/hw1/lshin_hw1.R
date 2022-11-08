#### Homework 1 ####
# Lucas Shin
# Intro to Data Science
# 9/23/2020

library(openintro)
library(tidyverse)
View(mariokart)


# 1
mariokart %>%
  filter(wheels >= 1) %>%
  arrange(total_pr) %>%
  select(title)


# 2
mariokart %>% 
  select(ship_sp, 
         ship_pr) %>% 
  group_by(ship_sp) %>% 
  summarize(range = max(ship_pr)-min(ship_pr)) 

  
# 3
mariokart %>% 
  group_by(cond) %>% 
  summarize(mean = mean(total_pr), median = median(total_pr))


# 4
library(ggplot2)
library(openintro)

mariokart %>% 
  filter(cond %in% c("new")) %>% 
  ggplot(mapping = aes(x = total_pr)) +
  geom_density()

mariokart %>% 
  filter(cond %in% c("used")) %>% 
  ggplot(mapping = aes(x = total_pr)) +
  geom_density()


# 5 
mariokart %>%
  ggplot(aes(x = start_pr,
             y = total_pr)) +
  geom_point()

mariokart2 <- mariokart %>%
  filter(total_pr < 100)

mariokart2 %>%
  ggplot(aes(x = start_pr,
             y = total_pr)) +
  geom_point()


# 6 
mariokart2 %>% 
  select(cond, wheels, total_pr) %>% 
  group_by(cond, wheels == 0) %>% 
  summarize(median = median(total_pr))
  


# 7
mariokart %>% 
  group_by(cond) %>% 
  summarize(proportion = mean(total_pr <= 50, na.rm = TRUE))


# 8

  
