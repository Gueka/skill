skill


No olvidarse de buildear el proyecto antes de levantarlo
gradle build

Levantar la aplicacion ejecutando este comando desde una terminal en la raiz del proyecto
gradle -Pprofiles=dev bootRun

Ejecutar este request para pegarle a la app levantada
curl --request POST \
  --url http://localhost:8443/alexa \
  --header 'Content-Type: application/json' \
  --header 'cache-control: no-cache' \
  --data '{
    "request": {
        "dialogState": "STARTED",
        "intent": {
            "confirmationStatus": "NONE",
            "name": "Hello"
        },
        "locale": "en-US",
        "requestId": "amzn1.echo-api.request.49c50248-02c2-4e25-8663-135f747f3df0",
        "timestamp": "2018-11-21T18:13:08Z",
        "type": "IntentRequest"
    },
    "session": {
        "new": true
    },
    "version": "1.0"
}'