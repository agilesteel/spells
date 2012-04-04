[Java 7]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[SBT 0.11]: https://github.com/harrah/xsbt/wiki

# spells 1.0

This is a small scala "util" library, which will hopefully grow over time.

### Features:

* AnsiPrint - ansi styled outputs for your strings

## Setup

### Requirements 

* [Java 7]
* [SBT 0.11] or greater
* Internet connection (in order for SBT to be able to download the necessary dependencies)

### Build

1. Get the source code:

		$ git clone https://github.com/agilesteel/spells.git
		$ cd spells

2. Launch SBT:

		$ sbt

3. Compile/Test:

		$ compile
		$ test