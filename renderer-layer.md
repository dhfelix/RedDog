---
title: Rdap Renderer Layer
---
# Rdap Renderer Layer

The RedDog RDAP server, after forming the response of a user request, delegates the responsibility of rendering the result to an implementation of the renderer-api.

The implementation is chosen based on the MIME type (Content-type) requested by the user at the time of the request.

First, the server gets the MIME type requested by the user, it is checked in the renderer.properties configuration if the MIME type requested by the user is mapped to some renderer implementation.

If the MIME type is mapped, the response will be rendered with the configured implementation, otherwise, if the MIME type requested by the user does not exist in the configuration, a default implementation is chosen that has been configured for any unregistered MIME type.


The RDAP team offers two reference implementations of rdap-renderer-api:

+ __[rdap-json-renderer](https://github.com/NICMx/rdap-json-renderer)__, this renderer prints the output of the requests in the JSON format as indicated by RFC7483.
+ __[rdap-text-renderer](https://github.com/NICMx/rdap-text-renderer)__, this renderer prints the output in plain text, in a format similar to WHOIS responses.

## Configuring renderers.properties

To tell the RDAP RedDog server which renderer implementations to use, you need to configure the renderers.properties file.

Here's how this property file should be configured

### keys

### `renderers`

List of names of the renderers to be configured, each name will be separated by a comma, and each name should not have space.

| Required? | Type | Default | Example |
|--------------------|--------|---------|-------------|
| :white_check_mark: | String (or List separated by a comma) | NO default value | renderers = json |

For each name configured in the renderers property, it is necessary to configure three attributes, __{renderer_name}.*__.

### `{renderer_name}.class`

Indicates the renderer's rendering class, and will be used by the server to create an instance of that renderer class.

| Required? | Type | Default | Example |
|--------------------|--------|---------|-------------|
| :white_check_mark: | String | :x: | json.class = mx.nic.rdap.renderer.json.JsonRenderer |


### `{renderer_name}.main_mime`

Indicates that the MIME type will be mapped to the renderer indicated in the .class attribute, in addition this MIME type will be added to the headers of the server response as the MIME type used to respond.

| Required? | Type | Default | Example |
|--------------------|--------|---------|-------------|
| :white_check_mark: | String | :x: | json.main_mime = application/rdap+json |

```
The structure of a MIME type is very simple; it consists of a type and a subtype, 
two strings, separated by a '/'. No space is allowed. The type represents the category 
and can be a discrete or a multipart type. The subtype is specific to each type.

A MIME type is insensitive to the case, but traditionally is written all in lower case.
```

### `{renderer_name}.mimes`

List of MIME types separated by commas, these MIME types will also be mapped to the renderer indicated in the .class attribute, but unlike .main_mime, these MIME types will not be published in the Server response headers.

| Required? | Type | Default | Example |
|--------------------|--------|---------|-------------|
| :x: | String | :x: |  json.mimes = application/json, application/html |




### `default_renderer`

Sets the renderer name to act as the default renderer for any MIME type. The name must be one of the configured in the renderers property.

| Required? | Type | Default | Example |
|--------------------|--------|---------|-------------|
| :white_check_mark: | String | :x: | default_renderer = json |

