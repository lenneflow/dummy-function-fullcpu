This a a dummy function for the lenneflow application. 
The function will process some cpu intensive calculation in order to use 100% cpu during a given time.<br>
The idea is to use this function to test the vertical and the horizontal scalability of the application

Input payload example
```json
{
    "callBackUrl" : "http://localhost:8100/api/qms/callback/1/1/1",
    "inputData": {
        "durationInSeconds": 25
        }
}
```



Output payload example
```json
{
    "runStatus" : "COMPLETED",
    "failureReason": "",
    "outputData": {
        "durationInSeconds": 25
        }
}
