# Simulation of Fast Uniform Dispersion of a Crash-prone Swarm

This program simulates the fast uniform dispersion of a crash-prone swarm. It's absolutely fantastic.

## Dependencies

+ Git
+ Maven
+ Java (SDK 14)

## How to run

The application has three components: a simulator with visual representation of the graph and robots; a non-visual simulator which creates statistics and results; and a maze generator. In the following we specify the way of running them. If you are using Linux, it will be a lot easier.

Arguments specified:
+ ```<input-path>``` Path to the input file, containing a maze.
+ ```<output-path>``` Path to the output file where results are expected.
+ ```<number-of-sources>``` Number of source nodes - integer greater or equal than 1.
+ ```<crash-chance>``` The chance of a robot crashing - real number between 0 and 1.
+ ```<number-of-simulations>``` Number of simulations - integer greater or equal than 0.
+ ```<dimensions>``` Size of the maze - integer greater or equal than 1.
+ ```<thread-wait>``` Speed of the robots and display - integer recommended between 10 and 500.

### Simulator with visual representation

This component shows a beautiful visual representation of the fast uniform dispersion of a crash prone swarm. __EPILEPSY WARNING!__

```sh
./i-want-to-see-robots <input-path> <number-of-sources> <crash-chance> <thread-wait>
```

### Simulator WITHOUT visual representation

```sh
./generate-results <input-path> <output-path> <number-of-sources> <crash-chance> <number-of-simulations>
```

### Maze generator

```sh
./give-me-a-maze-thanks <output-path> <dimensions>
```
