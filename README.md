# Simulation of Fast Uniform Dispersion of a Crash-prone Swarm

This program simulates the fast uniform dispersion of a crash-prone swarm. It's absolutely fantastic.

## Dependencies

+ Git
+ Maven
+ Java (SDK 14)

## How to run

To run the application you should run these commands:

```sh
git clone https://github.com/sajtizsolt/szhor
cd szhor
mvn clean install
java -cp szhor-simulator/target/szhor-simulator-<version>-jar-with-dependencies.jar hu.elte.szhor.Main --input <maze-filepath> --output <output-filepath> --chance-of-crash <float[0,1]>
```

The results will be in the output file.

## How to generate a custom maze

```sh
git clone https://github.com/sajtizsolt/szhor
cd szhor
mvn clean install
java -cp maze-generator/target/maze-generator-<version>-jar-with-dependencies.jar hu.elte.szhor.GeneratorApp --input <maze-filepath> --rows <number-of-rows> --columns <number-of-columns>
