# Properties

## Universal Properties
```xml
<application-name>Mall</application-name>

<server-port>8080</server-port>

<page-size>15</page-size>
```

## Extension Properties
### cross-origin
```xml
<cross-origin>
    <allowed-headers>
        <allowed-header>X-Custom-Header</allowed-header>
    </allowed-headers>

    <allowed-methods>
        <allowed-method>GET</allowed-method>
        <allowed-method>POST</allowed-method>
    </allowed-methods>

    <allowed-origins>
        <allowed-origin>*</allowed-origin>
    </allowed-origins>
</cross-origin>
```