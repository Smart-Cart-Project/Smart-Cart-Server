# Smart Cart Server

Server application created for the **Smart Cart TUM** project during the **Introduction to CPS** course.

## Requirements

- System-specific [Docker](https://docs.docker.com/engine/install/).
- [Java SE Development Kit 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
- Kubernetes [Minikube](https://minikube.sigs.k8s.io/docs/start/).
- [Gradle](https://gradle.org/install/) (Optional).
- [Firebase](https://firebase.google.com/) instance, required to store the data.
- [Ngrok](https://ngrok.com/) account to expose the server to the internet.
- Tmux to run the server on the background and be able to access it at any time (Optional).

## Server Structure

![Component Diagram](docs/Component%20Diagram.png)

## Functionality

Everything is stored in the corresponding collection/document in the database.<br>
Every request is done with the use of query parameters: **cartId** and **itemId**.

- Store the items in the cart.
- Update the items quantity in the cart.
  Increase the quantity for the **POST** request and decrease the quantity for the **DELETE** request, only if the
  item already exists.
- Delete the item from the cart if the quantity of the item is one.
- Retrieve the list of all items in the cart.

## Database Setup

You should perform the following steps in your **Firebase** instance:

- Create an **items** collection, it will store the available items, this collection won't be changed by the server.
  Must be filled in manually or create a new class/method to do it automatically.
- Create a **registered-items** collection, it will store IDs of the existing carts, this collection won't be changed
  by the server. Must be filled in manually or create a new class/method to do it automatically.
- Create a **carts** collection, it will store the cart document, this document will include the collections of the
  carts items that actually contains the items documents. Updated by the server.

## Server Setup

To run the server perform the following steps:

- Create a file named **serviceAccount.json** in the **/src/main/resources/** folder that will contain **Firebase**
  admin credentials, see [Admin SDK](https://firebase.google.com/docs/admin/setup). It is required to access the
  database.
- Connect to your ngrok account, follow the official [setup](https://dashboard.ngrok.com/get-started/setup).
- Simply run the **deploy.sh** script, read the comments to understand what it does.
- To clean up run the **cleanup.sh** script, read the comments to understand what it does.
