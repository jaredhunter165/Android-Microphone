# Android Microphone

This project is a continuation of a project I worked on with a small team during my senior year of college. The idea of this project is to utilize an android device as a microphone to send audio to a computer acting as a server in an auditorum conference type setting. The project contains two parts, a java desktop application that moderates and sets up UDP connections with android phones and an android application that sends packets to the server.

#Current State

Currently I have fairly clear audio being sent and recived by the server application. However there does seem to be a UDP "jitter" effect that creates a constant clicking noise. 