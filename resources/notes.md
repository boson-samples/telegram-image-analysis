# Deep Learning with OpenShift Serverless Functions

In this demo, you will learn how Serverless Functions can be used to do some
cool stuff with deep learning. The application we will build processes images,
converting them into what appear to be paintings.


## Setup

* OpenShift 4.6.1
* Red Hat OpenShift Serverless Operator 1.12.0
  * To install on a cluster that does not have this available, clone
    [the serverless operator repository](https://github.com/openshift-knative/serverless-operator),
    ensure that you are logged into your OpenShift cluster and run
    `make images install`
* Camel K Operator 1.12.0
* Knative Apache Camel OPerator 0.15.1

### Create the default broker

```
oc label namespace default eventing.knative.dev/injection=enabled
```

### Install resources

The demo uses Telegram to send images to a bot. A `CamelSource` is used to receive
these images and convert them to a `CloudEvent` which can then be sent to a Knative
event sink to be processed by functions. Install the Telegram `CamelSource`.

```
kubectl apply -f source_telegram.yaml
```

### Create the image processor

```
func createn image-processor -l node -t events
```

## Architecture

The demo consists of four functions.

* `receiver`: a function to receive an uploaded image and store it in a
  persistent volue
* `processor`: a function to process received images
* `notifier`: a function to notify (what exactly is tbd) an operator or user of
  some sort that the image has been processed and provide a download URL
* `displayer`: a function to 

# Create a function to receive an image upload