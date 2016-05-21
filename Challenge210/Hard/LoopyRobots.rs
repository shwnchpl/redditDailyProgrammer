// Compiled in rustc 1.0.0-beta (9854143cb 2015-04-02) (built 2015-04-02)
// Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/32vlg8/20150417_challenge_210_hard_loopy_robots/

use Direction::{North, South, East, West};
use std::io;

enum Direction {
    North,
    South,
    East,
    West,
}

struct Robot {
    bearing: Direction,
    position: (i32, i32),
}

impl Robot {
    fn new(pos: (i32, i32), bearing: Direction) -> Robot {
        Robot {
            position: pos,
            bearing: bearing,
        }
    }

    fn turn_right(&mut self) -> &mut Robot {
        match self.bearing {
            North => self.bearing = East,
            East => self.bearing = South,
            South => self.bearing = West,
            West => self.bearing = North,
        }

        self
    }

    fn turn_left(&mut self) -> &mut Robot {
        match self.bearing {
            North => self.bearing = West,
            West => self.bearing = South,
            South => self.bearing = East,
            East => self.bearing = North,
        }

        self
    }

    fn step(&mut self) -> &mut Robot {
        match self.bearing {
            North => self.position.1 += 1,
            West => self.position.0 += -1,
            South => self.position.1 += -1,
            East => self.position.0 += 1,
        }

        self
    }

    fn execute(&mut self, line: String) -> &mut Robot {
        for char in line.chars() {
            match char {
                'S'|'s' => self.step(),
                'R'|'r' => self.turn_right(),
                'L'|'l' => self.turn_left(),
                _ => continue,
            };
        }

        self
    }
}

fn main() {
    let mut robot = Robot::new((0,0), North);

    let mut input : String = String::new();
    io::stdin().read_line(&mut input).unwrap();
    input.pop();

    for i in 1..5 {
        robot.execute(input.to_string());

        match robot.bearing {
            North => if robot.position == (0,0) {
                println!("Looped after {} cycles", i);
                return } else { break },
            _ => continue,
        };
    }

    println!("No loop detected.");
}
