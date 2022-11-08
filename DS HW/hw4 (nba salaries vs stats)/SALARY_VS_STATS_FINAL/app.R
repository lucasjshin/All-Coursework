# Lucas Shin, Mariana Zamorano, Kaela Finegan, Cody Kim

library(shiny)
library(tidyverse)
library(ggplot2)
library(stringr)
library(shinyWidgets)
library(scales)
library(plotly)
library(htmlwidgets)
library(DT)
library(data.table)

nbaSalaries <- read_csv("nbaSalaries.csv")
advanced1920 <- read_csv("2019.2020advancedStats.csv")
perGame1920 <- read_csv("2019.2020perGameStats.csv")

statsPlayers <- c() # list of all the players in perGame1920 (perGame1920 and advanced1920 have the same players)


for (i in 1:nrow(perGame1920)) {
    statsPlayers <- c(statsPlayers, perGame1920$Player[i])
}

salaryPlayers <- c()
for (j in 1:nrow(nbaSalaries)) {
    salaryPlayers <- c(salaryPlayers, perGame1920$Player[j])
}

nbaSalariesFiltered <- nbaSalaries %>% 
    filter(Player %in% statsPlayers) %>% 
    dplyr::select(Player, `2019-20`, `2020-21`, `2021-22`, `2022-23`, `2023-24`, `2024-25`, `Guaranteed\\`)

# remove punctuation from entries in Guaranteed column
nbaSalariesFiltered$Guaranteed <- str_remove(nbaSalariesFiltered$`Guaranteed\\`,
                                             "[:punct:]")
nbaSalariesFiltered <- nbaSalariesFiltered %>% 
    dplyr::select(Player, `2019-20`, `2020-21`, `2021-22`, `2022-23`, `2023-24`, `2024-25`, Guaranteed)

perGame1920Filtered <- perGame1920 %>% 
    dplyr::select(!GS)

advanced1920Filtered <- advanced1920 %>% 
    dplyr::select(Player, PER, `TS%`, `USG%`, OBPM, DBPM, BPM)

# joined nbaSalaries with perGame1920
salaryAndPerGame <- left_join(nbaSalariesFiltered,
                              perGame1920Filtered,
                              by = "Player")

# joined salaryAndPerGame with advanced1920
allData <- left_join(salaryAndPerGame,
                     advanced1920Filtered,
                     by = "Player")

options(scipen = 50000000, digits = 10)

# get rid of \
colnames(allData) <- str_replace_all(colnames(allData),
                                     "\\\\",
                                     "")
allData$PTS <- str_remove(allData$PTS,
                          "\\\\")

# change '3' to 'three'
colnames(allData) <- str_replace_all(colnames(allData),
                                     "3",
                                     "three")
# change '%' to 'Percent'
colnames(allData) <- str_replace_all(colnames(allData),
                                     "%",
                                     "Percent")

# rename columns
colnames(allData) <- c('Player', 'szn2020', 'szn2021', 'szn2022', 'szn2023', 'szn2024', 'szn2025', 'Guaranteed', 'Position', 'Age', 'Team', 
                       'GamesPlayed', 'MinutesPlayed', 'FGM', 'FGA', 'FGPercent', 'ThreePointersMade', 'ThreePointAttempts', 'ThreePointPercent', 
                       'TwoPointersMade', 'TwoPointAttempts', 'TwoPointPercent', 'eFGPercent', 'FTM', 'FTA', 'FTPercent', 'ORB', 'DRB', 'TRB', 'AST', 
                       'STL', 'BLK', 'TOV', 'PF', 'PTS', 'PER', 'TSPercent', 'USG', 'OBPM', 'DBPM', 'BPM')

# Put this in to get rid of everything after the \\ in the player names
allData$Player <- sub("\\\\.*", "", allData$Player)

# make numerical instead of categorical
allData$PTS <- as.numeric(allData$PTS)

allData2 <- allData %>% 
    dplyr::select(Player, Team, szn2020, szn2021, szn2022, szn2023, szn2024, szn2025, Guaranteed, PTS, AST, ORB, DRB, TRB, STL, BLK, TOV, PF, FGM, FGA, FGPercent, FTM, FTA, FTPercent, ThreePointersMade, ThreePointAttempts, ThreePointPercent, 
                  TwoPointersMade, TwoPointAttempts, TwoPointPercent, GamesPlayed, MinutesPlayed, PER, eFGPercent, TSPercent, USG, OBPM, DBPM, BPM)

allData3 <- allData2 %>% 
    mutate(Conference = ifelse(Team %in% c("MIL", "TOR", "BOS", "IND", "MIA", "PHI", "BRK","ORL",
                                           "CHO", "WAS", "CHI", "NYK", "DET", "ATL", "CLE"),
                               "Eastern",
                               "Western"))

modes <- c("r", "r", "r", "r", "r", "r", "a", "a", "a", "a", "a", "a")
years <- c("szn2020", "szn2020", "szn2021", "szn2021", "szn2022", "szn2022", "szn2023", "szn2023", "szn2024", "szn2024")
years2 <- c(allData3$szn2020, allData3$szn2020, allData3$szn2021, allData3$szn2021, 
            allData3$szn2022, allData3$szn2022, allData3$szn2023, allData3$szn2023, 
            allData3$szn2024, allData3$szn2024)
stats <- colnames(allData3)[10:39]

# rsquared_vals <- matrix(0L, nrow = length(stats), ncol = length(modes)) 
# for(mode in 1:12)
#     for(stat in 1:length(stats)){
#         if(modes[mode] == "r"){
#             element <- summary(lm(stats[stat] ~ years2[mode], data = allData3))$r.squared
#             rsquared_vals[stat, mode] <- element
#         }
#         else{
#             element <- AIC(lm(stats[stat] ~ years2[mode], data = allData3))
#             rsquared_vals[stat, mode] <- element
#         }
#     }


allData_noNA <- allData3[complete.cases(allData3[, -(1:9)]),]
rSquared_vals <- data.frame((cor(dplyr::select(allData_noNA, stats, szn2020)))^2)
updated1 <- data.frame(t(rSquared_vals[31 ,]))
updated2 <- mutate(updated1, var = rownames(updated1))
updated3 <- data.frame(updated2$var, updated2$szn2020)
colnames(updated3) <- c("Stat", "'19-'20 Salary")
updated4 <- updated3 %>% 
    arrange(desc(`'19-'20 Salary`))

updated5 <- updated4[2:31,]





ui <- fluidPage(
    titlePanel(h1(id = "heading", "NBA Statistics vs. Salary (By Year)", align = "center")),
    tags$style(HTML("#heading{color: ghostwhite;}")),
    setBackgroundColor(
        color = c("#F6FBFB", "orange"),
        gradient = "linear",
        direction = "top"
    ),
    sidebarLayout(
        sidebarPanel(
            selectizeInput(inputId = "var1",
                           label = "Choose a statistic to plot against salaries",
                           choices = colnames(allData2)[-(1:9)]),
            selectizeInput(inputId = "var2",
                           label = "Choose what Conferences to display",
                           choices = c("Both", "Eastern", "Western")),
            p("Statistic Explanations",
              br(), 
              br(),
              "•FGM = Field Goals Made / Game",
              br(),
              "•eFGPercent = Weighted measure of field goal percentage to account for the fact that three pointers count for more points than two pointers",
              br(),
              "•FTM = Free Throws Made / Game",
              br(),
              "•FTA = Free Throw Attempts / Game",
              br(),
              "•ORB = Offensive Rebounds / Game",
              br(),
              "•DRB = Defensive Rebounds / Game",
              br(),
              "•TRB = Total Rebounds / Game",
              br(),
              "•PER = Measure of per-minute productivity, summing up a player's positive stats and subtracting negative stats during a game",
              br(),
              "•TSPercent = A measure of scoring efficiency based on the number of points scored over the number of possessions in which they attempted to score",
              br(),
              "•USG = Measure of how much a player is used when on the court",
              br(),
              "•OBPM = Measure of the average # of points a player's team scores per 100 possessions when a specific player is on the court",
              br(),
              "•DBPM = Measure of the average # of points a player's team allows per 100 possessions when a specific player is on the court",
              br(),
              "•BPM = Measure of the average point differential per 100 possessions when a specific player is on the court")),
        mainPanel(
            tabsetPanel(
                tabPanel("Report", p(strong("Introduction")), span("This application analyzes the relationship between NBA player salaries (according to their current 
                                       guaranteed contracts) and various basketball statistics recorded during the 2019-2020 NBA season. 
                                       Most of the time, players with the most value are given contracts that span 
                                       over longer periods of time. The following tabs filter the players based on how long their contracts
                                       last. For example, Ben Simmons, Jamal Murray, and Damian Lillard--all of whom are all star caliber players--are 
                                       the only players who have contracts that last until the 2025-2026 season, so they are the only players included 
                                       in the tab labeled '2025-2026 SZN'. On the other hand, younger players like Miye Oni and Malik Newman (who are still 
                                       fighting to find a place in the NBA) have not yet earned multi-year contracts, so they are only included in the 
                                       tab labeled '2019-2020 SZN'. In general, the better players have longer spanning contracts. Each year/season tab 
                                        indicates the amount of money that each player is projected to make in that given year. For each graph, you can hover 
                                                                   over a point to see the player's name.", br(),br(), 
                                                                   strong("Research Question:"), br(), "After analyzing the data, we hope to see which variable has 
                                        the strongest correlation with salary.", br(), br(), strong("Keystone Graphic"), plotlyOutput("Keystone"), br(), br(), strong("Methods/Results"), br(), 
                                                                   "To answer our research question, we decided to make our app print the R-Squared value for
                                        each graph. After analyzing each variable against salary, the user can determine which variable has
                                        the strongest R-Squared value. We ran some diagnostic tests and determined that Points (PTS) has
                                        the strongest R-Squared value and thus the strongest correlation with salary." ), br(), br(), span("We must look at these diagnostic graphs in order to proceed with our 
                                                                                                                                     models because we have to check that all assumptions are met. With linear 
                                                                                                                                     regression, we are trying to better understand the relationship between 
                                                                                                                                     our x and y variables. Salary is always the y variable for our graphs 
                                                                                                                                     and regressions. In our case, we are trying to see which variable is 
                                                                                                                                     most strongly correlated with salary. We have chosen to look at the 
                                                                                                                                     diagnostic plots for the PTS (points) variable because, after calculating 
                                                                                                                                     the R-squared value, we determined that this x variable has the strongest 
                                                                                                                                     correlation with salary. We have to meet four assumptions: our x and y 
                                                                                                                                     variables must be linearly related; our observations must be independent; 
                                                                                                                                     the residuals must be normally distributed for any given x; and our data 
                                                                                                                                     must be homoskedastic (there must be the same variance for all x). The x 
                                                                                                                                     and y variables must be linearly related for a linear regression model 
                                                                                                                                     because the goal is to show how the y variable changes depending on the 
                                                                                                                                     value of the x variable. We believe that players’ salaries are reasonably 
                                                                                                                                     independent of each other. One could argue that within a team, there can 
                                                                                                                                     only be so many max contract players and players with very high salaries 
                                                                                                                                     due to team salary caps, but for analysis of statistics and salaries on a 
                                                                                                                                     league-wide scale, the level of dependency is low enough that we consider 
                                                                                                                                     salaries to be independent. We used the plot() function (graphs shown below) 
                                                                                                                                     to check our linear model assumptions. The residual plot is mostly straight, 
                                                                                                                                     meaning that the residuals are normally distributed. The residuals versus 
                                                                                                                                     leverage plot, one of the most important plots, shows us how much influence 
                                                                                                                                     a certain point has on the slope of the line. There is not one specific 
                                                                                                                                     point that highly influences the slope of the line; they all fall within a 
                                                                                                                                     certain path/line. Therefore, this assumption is met."), br(), br()
                         , 
                         style = "color:black", 
                         plotOutput(outputId = "diagnostic"),
                         textOutput(outputId = "diagnostic_text"),
                         br(), p(strong("R-Squared Values for each Statistic vs. Salary")),
                         DT::dataTableOutput(outputId = 'table1'),
                         p(strong(br(), "Conclusion"), br(), "Based on the correlation table shown above, we found that total points scored was most highly correlated with salary in the 2019-2020 season. Our ultimate goal was to try to predict our y variable (salary) using some x variable, and we found that the points variable explains 42.15% of our y variable. This means there is a moderate positive correlation between salary and points. In general, players with more points have a higher salary.
")),
                tabPanel("'19-'20 Season", plotlyOutput(outputId = "plot1"), 
                         textOutput(outputId = "text1"),
                         textOutput(outputId = "text1b")),
                tabPanel("'20-'21 Season", plotlyOutput(outputId = "plot2"), 
                         textOutput(outputId = "text2"),
                         textOutput(outputId = "text2b")),
                tabPanel("'21-'22 Season", plotlyOutput(outputId = "plot3"), 
                         textOutput(outputId = "text3"),
                         textOutput(outputId = "text3b")),
                tabPanel("'22-'23 Season", plotlyOutput(outputId = "plot4"), 
                         textOutput(outputId = "text4"),
                         textOutput(outputId = "text4b")),
                tabPanel("'23-'24 Season", plotlyOutput(outputId = "plot5"), 
                         textOutput(outputId = "text5"),
                         textOutput(outputId = "text5b")),
                tabPanel("'24-'25 Season", plotlyOutput(outputId = "plot6"), 
                         textOutput(outputId = "text6"),
                         textOutput(outputId = "text6b"))
            )
        )
    )
)

server <- function(input, output) {
    output$diagnostic <- renderPlot({
        lm1 <- lm(PTS ~ szn2020, data = allData3)
        # Graphs we want: 
        #Residual plot -- We would want the residuals being close to zero (normally distributed)
        #Residuals vs leverage -- most useful, how much influence that point has on the slope
        #Cooks distance similar to a contour plot
        par(mfrow = c(2,2))
        plot(lm1)
    })
    output$diagnostic_text <- renderText({
        lm1 <- lm(PTS ~ szn2020, data = allData3)
        sum <- summary(lm1)
        paste("R-Squared for Points (PTS): ", sum$r.squared)
    })
    
    output$table1 = DT::renderDataTable({
        setDT(updated5)
    })
    
    output$plot1 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot1 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2020",
                              text = paste('Player'))) + 
            geom_point() + 
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '19-'20 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot1, tooltip = c("text"))
        
    })
    
    output$text1 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2020, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text1b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2020, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$plot2 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot2 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2021",
                              text = paste('Player'))) + 
            geom_point() +
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '20-'21 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot2, tooltip = c("text"))
    })
    
    output$text2 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2021, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text2b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2021, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$plot3 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot3 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2022",
                              text = paste('Player'))) + 
            geom_point() +
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '21-'22 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot3, tooltip = c("text"))
    })
    
    output$text3 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2022, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text3b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2022, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$plot4 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot4 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2023",
                              text = paste('Player'))) + 
            geom_point() + 
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '22-'23 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot4, tooltip = c("text"))
    })
    
    output$text4 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2023, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text4b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2023, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$plot5 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot5 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2024",
                              text = paste('Player'))) + 
            geom_point() + 
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '23-'24 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot5, tooltip = c("text"))
    })
    
    output$text5 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2024, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text5b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2024, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$plot6 <- renderPlotly({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        scatterplot6 <- allData4 %>%  
            ggplot(aes_string(x = input$var1,
                              y = "szn2025",
                              text = paste('Player'))) + 
            geom_point() +
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle(paste(input$var1, " vs Salary for the '24-'25 Season", sep = "")) +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot6, tooltip = c("text"))
    })
    
    output$text6 <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2025, data = allData4)
        return(paste("R-Squared: ", summary(lm1)$r.squared))
    })
    
    output$text6b <- renderText({
        if (input$var2 == "Eastern") {
            allData4 <- allData3 %>% 
                filter(Conference == "Eastern")
        } else if (input$var2 == "Western") {
            allData4 <- allData3 %>% 
                filter(Conference == "Western")
        } else {
            allData4 <- allData3
        }
        lm1 <- lm(get(input$var1) ~ szn2025, data = allData4)
        return(paste("AIC: ", AIC(lm1)))
    })
    
    output$Keystone <- renderPlotly({
        scatterplot7 <- allData3 %>%  
            ggplot(aes_string(x = "PTS",
                              y = "szn2020",
                              text = paste('Player'))) + 
            geom_point() + 
            labs(y = "Salary") +
            theme_bw() + 
            scale_y_continuous(labels = comma, breaks=seq(0, 60000000, by = 5000000),
                               limits = c(0,60000000)) +
            ggtitle("PTS vs Salary for the '19-'20 Season") +
            theme(plot.title = element_text(hjust = 0.5))
        
        ggplotly(scatterplot7, tooltip = c("text"))
    })
}

# Run the application 
shinyApp(ui = ui, server = server)
