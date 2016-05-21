// Compiled in rustc 1.0.0-beta (9854143cb 2015-04-02) (built 2015-04-02)
// Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/33ow0c/20150424_challenge_211_hard_hungry_puppies/

extern crate rand;
use std::io;
use rand::distributions::{IndependentSample, Range};

trait Puppies {
    fn calc_score(&self) -> i32;
    fn mutate(&mut self) -> &Self;
}

impl Puppies for Vec<u32> {
    fn calc_score(&self) -> i32 {
        let mut score: i32 = 0;
        let len = self.len() - 1;

        score += if self[0] > self[1] { 1 }
             else if self[0] < self[1] { -1 }
             else { 0 } +
             if self[len] > self[len - 1] { 1 }
             else if self[len] < self[len - 1] { -1 }
             else { 0 };

        for i in 1..len {
            score += if self[i] > self[i - 1] &&
                    self[i] > self[i + 1] { 1 }
                 else if self[i] < self[i - 1] &&
                    self[i] < self[i + 1] { -1 }
                 else { 0 };
        }

        score
    }

    fn mutate(&mut self) -> &Self {
        let len = self.len() - 1;
        let between = Range::new(0, len);
        let mut rng = rand::thread_rng();
        let randx = between.ind_sample(&mut rng);
        let randy = between.ind_sample(&mut rng);

        let swap = self[randx];
        self[randx] = self[randy];
        self[randy] = swap;
        self
    }
}

fn main() {
    let mut stdin = io::stdin();
    let mut input = "".to_string();
    let mut max_score: i32;
    let mut best_order: Vec<u32>;

    let _ = stdin.read_line(&mut input);

    let mut int_input: Vec<u32> = input.trim().split(" ")
        .map(|n| match n.parse::<u32>() {
            Ok(n) => n,
            _ => panic!("Syntax error on imput."),
        })
        .collect();

    max_score = int_input.calc_score();
    best_order = int_input.clone();

    let mut stable_count = 0;

    while stable_count <= 2048 {
        let old_order = int_input.clone();
        let new_score = int_input.mutate().calc_score();

        if new_score < old_order.calc_score() {
            int_input = old_order;
            stable_count += 1;
            continue
        }

        if new_score > max_score {
            stable_count = 0;
            max_score = new_score;
            best_order = int_input.clone();
            println!("Max score so far is {}", max_score);
            println!("Order is {:?}", best_order);
        }
    }

    println!("After 2048 cycles, max score of {} is stable.
Order is {:?}", max_score, best_order);
}
