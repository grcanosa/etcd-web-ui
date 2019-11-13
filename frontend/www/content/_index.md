---
title: Presence Control Excercise
markup: "markdown"
---

## Presence Control Excercise

Design and implement the backend for a presence control system for the employees of a company. You need to take into consideration that the employees must always clock in and out by registering their fingerprint.

The “People” department will need:

* Access to the employee’s presence and time control.
* Generation of time sheets of effective worked hours.
* Reception of asynchronous notifications regarding: total absence time, absence anomalies (for example because of on-call duties), etc.

### Discussion

A problem as open as this raises a number of question that, if this were a real-life project, will need to be presented to the client. The main questions that come to my mind are:

* Does the company already has a fingerprint identification service?
* Will the system be used for presence control only? Will it also be used as access control?
* Are all the scanners the same or are there different scanners for clock-in and clock-out operations?
* Will the employees work overnight?
* What type of granularity does the companmy wants in its effective worked hours sheets?
* Do they want aggregation over departments or teams?
* What constitutes an anomaly?

