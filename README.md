# Test

## Upload a file
Send POST request to URL http://localhost:8080/api/csv/upload with form-data, key=file, and the file as a value:
![image](https://user-images.githubusercontent.com/44066233/198557764-5c49f831-143e-4121-8628-b9daef58abf1.png)

## Get all records
In browser hit a URL: http://localhost:8080/api/csv/download/all

## Get a record by code
In browser hit a URL: http://localhost:8080/api/csv/download/code/http://localhost:8080/api/csv/download/code/415974002

## Delete all records
Send POST-request to URL: http://localhost:8080/api/csv/delete/all
