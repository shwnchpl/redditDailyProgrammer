// Compiled in rustc 1.0.0-beta (9854143cb 2015-04-02) (built 2015-04-02)
// Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/358pfk/20150508_challenge_213_hard_stepstring_discrepancy/

macro_rules! max {
    ($a:expr, $b:expr) => (
        if $a > $b { $a } else { $b }
    )
}

fn main() {
    let mut input = "".to_string();
    let mut stdin = std::io::stdin();
    let mut high_dep = 0;

    let len = stdin.read_line(&mut input).unwrap() - 1;
    input.pop();

    let disc_vec: Vec<isize> =
        input.chars().map(|e| if e == 'a' { 1 } else { -1 }).collect();

    for step in 1..len {
        for start in 0..step {
            if (len - start) / step + 1 < high_dep as usize { break }

            let (mut pos, mut neg, mut mpos, mut mneg) = (0, 0, 0, 0);
            let mut index = start;

            while index < len {
                pos = max!(0, pos + disc_vec[index]);
                mpos = max!(mpos, pos);

                neg = max!(0,  neg - disc_vec[index]);
                mneg = max!(mneg, neg);

                index += step;
            }

            let max_mnmp = max!(mneg, mpos);
            high_dep = max!(high_dep, max_mnmp);
        }
    }

    println!("{}", high_dep);
}
