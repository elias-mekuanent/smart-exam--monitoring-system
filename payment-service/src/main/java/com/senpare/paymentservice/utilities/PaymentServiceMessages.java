package com.senpare.paymentservice.utilities;

public class PaymentServiceMessages {

    public static String canNotBeNull(String resourceName) {
        return String.format("%s can't be null", resourceName);
    }

    public static String canNotBeFound(String resourceName, String identifierName, String identifier) {
        return String.format("%s can't be found by %s: %s", resourceName, identifierName, identifier);
    }

    public static String alreadyExists(String resourceName, String identifierName, String identifier) {
        return String.format("%s already exists by %s: %s", resourceName, identifierName, identifier);
    }

    public static String canNotDeleteResourceWithAssociatedResource(String resourceName, String associatedResourceName){
        return String.format("Can't delete %s that has associated %s ", resourceName, associatedResourceName);
    }
}
