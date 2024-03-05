FROM openjdk:17-jdk-alpine
ADD target/ecommerce-0.0.1-SNAPSHOT-exec.jar app.jar
ADD src/main/resources/bulk_upload.xlsx src/main/resources/bulk_upload.xlsx
ADD public/* public/
ENTRYPOINT ["sh","-c","java -jar /app.jar"]