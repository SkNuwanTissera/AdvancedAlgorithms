import ballerinax/trigger.shopify;
import ballerina/http;

configurable shopify:ListenerConfig config = ?; 

listener http:Listener httpListener = new(8090);
listener shopify:Listener webhookListener = new(config, httpListener);

service shopify:CustomersService on webhookListener {
    
    remote function onCustomersCreate(shopify:CustomerEvent event ) returns error? {
      //Not Implemented
    }
    remote function onCustomersDisable(shopify:CustomerEvent event ) returns error? {
      //Not Implemented
    }
    remote function onCustomersEnable(shopify:CustomerEvent event ) returns error? {
      //Not Implemented
    }
    remote function onCustomersUpdate(shopify:CustomerEvent event ) returns error? {
      //Not Implemented
    }
    remote function onCustomersMarketingConsentUpdate(shopify:CustomerEvent event ) returns error? {
      //Not Implemented
    }
}

service /ignore on httpListener {}