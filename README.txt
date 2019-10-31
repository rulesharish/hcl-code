Create Account

post: http://localhost:18080/v1/accounts
payload: {"accountId":"1","balance":1000}


payload: {"accountId":"2","balance":1000}




transfer balance
post: http://localhost:18083/v1/accounts/transfer
Payload: {"fromAccountId":"1","toAccountId":"2","amountToTransfer":300}



