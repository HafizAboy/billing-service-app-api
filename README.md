# billing-service-app-api
Sample Rest API application that integrate with third party SOAP application

com.billing.app.noetic.wsdl - containing all the generated Java classes from .WSDL file.
com.billing.app.noetic.config - configuration files for converting and preparing REST API JSON to XML SOAP application readable.
com.billing.app.noetic.service - SOAP client service based on business rule or requirements, Spring Retry and email implemented here.
com.billing.app.service.EmailService - simple email service