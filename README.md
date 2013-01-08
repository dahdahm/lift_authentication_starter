lift_authentication_starter
===========================

A lift openid/Auth authentication sample (Google, Facebook, Twitter, etc)
~                                                                         

=== ABSTRACT ===

This is a project to show Authentication through OpenId and OAuth2.0

It is based on the Omniauth widget for auth 2.0 and various samples that I have found in the lift community.


=== PROJECT STRUCTURE ===

It contains:
- SBT project files
- Scala source code


=== HOW TO RUN ===

./Sbt:
If you use SBT simply cd to this directory and type './sbt'.
After this you can issue commands in sbt prompt:
> ; clean; compile; test
Alternatively you can type in shell:
> sbt update clean compile test

Eclipse:
Eclipse project can be generated with 'sbt eclipse'
plugin. Use 'Import New Projects' in eclipse to open it.

IntelliJ IDEA:
You can generate an IntelliJ project by running this command in sbt:
> gen-idea
Some available options are: no-classifiers no-sbt-classifiers.

======
This project is a work in progress and anyone can participate.


