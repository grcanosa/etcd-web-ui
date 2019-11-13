---
title: Solution
markup: "markdown"
---

## Solution

The provided solution is working and has the following features:

* Register a current presence via fingerprint. 
* Register a past or present virtual "presence" for remote working or other unexpected situations. 
* Get an employee presences for a given year and month.
* Get an employee effective hours for a given year and month.
* Get an employee total worked hours for a given year and month.
* Daily generation of anomalies report.
* Management of anomalies. 

### Assumptions

The following assumptions have been made in the presented solution:

* Employees do not work overnight. 
* Fingerprint scanners do not provide clock-in or clock-out information, only an employee presence.
* Fingerprints have been mapped to a "Long" and its match needs to be exact. 

#### Employee Management and Finterprint Matching

In order to provide a working solution, employees management services have been provided. These classes do not intend to be a full employee management service. They are only provided to have a working solution.
Also, fingerprint matching is usually performed by databases that have multiple threads running and  perform comparisons in [parallel](https://sci2s.ugr.es/ParallelMatching). Real fingerprint are usually stored as a bitmap and its comparison its never exact, but done by probabilities. 

### Proposed solution

The proposed solution is composed of two microservices: the presence service and the people service. 

#### Presence Service

This microservice is a lightweight application that just registers presence in the database. It could potentially be replicated as needed across different floors of even different buildings. Each of those copies, or a group of them, would work against a node of the Mongo database. The BASE (Basically Available, Soft State, Eventual consistency) architecture of this database would allow for internet failures as well as high load of presence checks. 

#### People Service

This service is intented to be single-instance and its in charge of answering the requests of the peple department. It can generate employee presence reports as well as employee effective hours reports. It also allows the people department to enter "virtual" presences for on-call visits, remote working days, etc... It also allows the people department to asynchronously check the anomalies in the employees presence and delete them when they have been solved.
A cron-like job should be added in the server to call the discover anomalies function once a day (for example at midnight), to ensure that the anomalies are always discovered and ready for the people department when they want them.

### Provided Frontend

A simple frontend to test the solution has also been provided. This frontend is served by an ExampleApp backend that merges the Presence and the People Service. 
In this frontend the user can generate some test data. This functionality would NEVER be shipped in a real-life product. 

## Future work

Future improvements to the presented solution:

* Support overnight work stays.
* Support aggregation for departments or teams. 
* Implement more anomalies detection.