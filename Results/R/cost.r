library(ggplot2)
library(reshape2)
library(gtable)
library(scales)

reader <- read.csv2("/home/vinicius/PIPEL2/resultados/graficosr/cost.csv", sep=";")


#ggplot(reader, aes(x=Scenario,y=Speedup)) + geom_boxplot()

# create dummy data
#data <- data.frame(  name=letters[1:5],  value=sample(seq(4,15),5),  sd=c(1,0.2,3,2,4))

ggplot(reader) +
  geom_bar( aes(x=Scenario, y=Cost), stat="identity", fill="orange", alpha=0.7) +
  #geom_errorbar( aes(x=Scenario, ymin=Cost-sd, ymax=Cost+sd), width=0.4, colour="black", alpha=0.9, size=0.5)+
  theme(text=element_text(family="Times"),axis.text.x=element_text(size=11,angle = 45, hjust = 1),axis.text.y=element_text(size=11))