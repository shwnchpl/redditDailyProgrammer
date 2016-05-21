
// Compiled in rustc 1.0.0-beta (9854143cb 2015-04-02) (built 2015-04-02)
// Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/40rs67/20160113_challenge_249_intermediate_hello_world/

extern crate rand;
extern crate time;
use time::PreciseTime;
use rand::distributions::{IndependentSample, Range};
use std::io;

trait Genetics {
    fn fitness(&self, target: &Vec<char>) -> u32;
    fn mutate(&mut self) -> &Self;
    fn generate_random(length: usize) -> Self;
}

impl Genetics for Vec<char> {
    fn fitness(&self, target: &Vec<char>) -> u32 {
        let mut fitness = 0u32;
        for i in 0..self.len() {
            fitness += (self[i] as i32
                    - target[i] as i32).abs() as u32;
        }

        fitness
    }

    fn mutate(&mut self) -> &Self {
        let len = self.len();
        let mut rng = rand::thread_rng();
        self[Range::new(0, len).ind_sample(&mut rng)] =
            Range::new(0x20, 0x7e).ind_sample(&mut rng)
                as u8 as char;
        self    
    }

    fn generate_random(length: usize) -> Vec<char> {
        let mut rand_vec: Vec<char> = (0us..length)
                .map(|e| e as u8 as char).collect::<Vec<char>>();
        let mut rng = rand::thread_rng();

        for i in 0..rand_vec.len() {
            rand_vec[i] = Range::new(0x20, 0x7e)
                .ind_sample(&mut rng) as u8 as char;
        }

        rand_vec
    }
}


fn main() {
    println!("Enter a string: ");

    let mut fit_string = "".to_string();
    let mut stdin = io::stdin();

    let _ = stdin.read_line(&mut fit_string);
    fit_string.pop();

    let mut test_vec: Vec<char> = Genetics::generate_random(fit_string.len());
    let fit_vec: Vec<char> = fit_string.chars().collect();

    let mut lowest_fit = test_vec.fitness(&fit_vec);

    let start = PreciseTime::now();

    for gen in 0.. {
        let old_vec = test_vec.clone();
        let mut_fit = test_vec.mutate().fitness(&fit_vec);

        if mut_fit < lowest_fit {
            lowest_fit = mut_fit;

            println!("Gen: {} | Fitness: {}\t| {}",
                gen, lowest_fit, test_vec.clone().iter().map(|c| *c).collect::<String>());

            if lowest_fit == 0 { break }
            continue
        }

        test_vec = old_vec;
    }

    let end = PreciseTime::now();

    println!("Elapsed time: {} milliseconds", start.to(end).num_milliseconds());
}
