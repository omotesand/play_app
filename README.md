**This is my application. You can check from [here](http://sentiment-app.com:8080/play).**
# Let's play with sentiment analysis AI ! 🧠
This is Spring Boot web application using sentiment analysis AI.

https://user-images.githubusercontent.com/100463080/170863455-86ae31dc-fd3d-4f10-9e63-000576a5dee6.mp4


# Technology
- Java 11.0.2
- Spring Boot 2.6.3
- MySQL 8.0.28
- AWS
  - VPC
  - EC2
  - RDS
  - Route 53
  - Amazon Comprehend (Learnd model of NLP)
- HTML/CSS
- Chart.js

# AWS Architecture Diagram

<img width="660" alt="my-infra" src="https://user-images.githubusercontent.com/100463080/169912654-d519a0c3-98e5-47f8-9071-b6241686d974.png">

Amazon Comprehend is Learnd model of Natural Language Processing, which is able to perform sentiment analysis.

When EC2 instance gets requests through form from the client, Spring application posts text to Comprehend by using AWS secret access key.
