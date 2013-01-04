ETString
========

**ETString** is a very simple Java version of *[NSString stringWithFormat: …]* in Objective-C.

## Installation

Just copy ETString.java to your project.

## Usage

There are two placeholders:
* %@ - Uses the .toString()-method
* %f - Uses the .toString()-method (similiar to %@)
* %.xf - Uses a NumberFormatter to cut to x decimal places
* %% - Adds a %

*In case of an error the placeholder will get replaced by a #.*

## Contact

[@einfallstoll](https://twitter.com/einfallstoll) on Twitter