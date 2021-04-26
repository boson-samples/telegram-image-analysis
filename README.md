# Telegram Image Analysis with OpenShift Serverless Functions

In this demo, you will learn how Serverless Functions can be used to do facial recognition in the Telegram chat. 

Here you can watch a recording of this demo:

[![OpenShift Serverless Functions - Telegram Image Analysis Demo](http://img.youtube.com/vi/CsYo0SmQ0Uk/0.jpg)](http://www.youtube.com/watch?v=CsYo0SmQ0Uk "OpenShift Serverless Functions - Telegram Image Analysis Demo")


## Setup

* OpenShift 4.6 (or newer)
* Red Hat OpenShift Serverless - installed as an operator from OperatorHub (version >= 1.11.0)
  * To install on a cluster that does not have this available, clone
    [the serverless operator repository](https://github.com/openshift-knative/serverless-operator),
    ensure that you are logged into your OpenShift cluster and run
    `make images install`
* Red Hat Integration - Camel K - installed as an operator from OperatorHub (version >= 1.2.0)

You will also need the following API keys and accounts

* [Microsoft Faces API Key](https://azure.microsoft.com/en-us/services/cognitive-services/face/)
* [Telegram Bot API KEY](https://core.telegram.org/bots)
* An image registry account such as docker.io or quay.io

### Set the API keys for Telegram and MS Faces API

* Set the Telegram API Key in the following files
  * `deploy/kamelet-broker.yaml`
  * `receiver/func.yaml`
  * `responder/func.yaml`
  * `responder/package.json`

* Set the Microsoft Faces API Key in the following files
  * `processor/func.yaml`

### Create the default broker

```
oc apply -f deploy/broker.yaml
```

### Connect to Telegram via CamelSource 

The demo uses Telegram to send images to a bot. A `CamelSource` is used to receive
these images and convert them to a `CloudEvent` which can then be sent to a Knative
event sink to be processed by functions. Install the Telegram `Kamelet` which handeles
the creation of `CamelSource` and binding it to the `Broker` for you..

```
kubectl apply -f kamelet-broker.yaml
```

### Create the image receiver

Export image registry that's being to used to store function images,
if you haven't done so:
```
export FUNC_REGISTRY=docker.io/johndoe
```

Deploy `receiver`:
```
kn func deploy -p receiver
```

And connect it to the Broker:
```
oc apply -f deploy/trigger-receiver.yaml
```

### Create the image processor

Deploy `processor`:
```
kn func deploy -p processor
```

And connect it to the Broker:
```
oc apply -f deploy/trigger-processor.yaml
```

### Create the responder

Deploy `responder`:
```
kn func deploy -p responder
```

And connect it to the Broker:
```
oc apply -f deploy/trigger-responder.yaml
```
