# Developer Guide
- Use Chapa test cards to test payment service. You can find them [here](https://developer.chapa.co/docs/testing-cards/).
- For now you may need to pass email address of a user for endpoints such as `/api/v1/payment-service/licenses/user?email=abebebikila@gmail.com` but once security is implemented, the backend will get the email address from the JWT token.
## Payment Initialization Flow
![Payment Service Architecture drawio](https://user-images.githubusercontent.com/78301074/234272651-b2f999f0-d3c1-4c89-803f-570f8226b37d.png)

## Payment Verification Flow
![Payment Service Architecture-Page-2 drawio](https://user-images.githubusercontent.com/78301074/234272674-85f908b5-1456-43cb-9be3-155a32d8acf5.png)
