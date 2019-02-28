library(ggplot2)
library(reshape2)
library(gtable)
library(scales)

reader <- read.csv2("/home/vinicius/PIPEL2/resultados/graficosr/heatmap2.out", sep=";")
#colnames(reader)[3] <- "Resources(%)"


ggplot(reader, aes(x=cont,y=experiment)) +
  geom_tile(aes(fill=percent)) +
  ylab(label="Experiments") +
  xlab(label="Time (s)") +
  theme(text=element_text(family="Times")) +
  theme(legend.position="bottom")+
  scale_x_continuous(breaks = seq(0, 3339, by = 500),expand = c(0.01,0.01))  +
  
  
  
  #scale_fill_gradient('percent', limits=c(0, 100),  low = "lightgreen", high = "black") 
  #scale_fill_gradient('percent', limits=c(0, 100),  low = "lightblue", high = "darkblue") 
  #scale_fill_gradient('percent', limits=c(0, 100),  low = "yellow", high = "red") 
  scale_fill_gradient('Workload Execution (%)', limits=c(0, 100),  low = "lightgreen", high = "black") 
#scale_fill_gradient('percent', limits=c(0, 100),  low = "lightblue", high = "black") 