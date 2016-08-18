{-# LANGUAGE OverloadedStrings #-}

import Data.Word(Word8)
import Data.Monoid((<>))
import Data.Bits((.&.), shiftL, shiftR)
import System.Environment(getArgs,getProgName)
import qualified Data.ByteString.Lazy as L
import qualified Data.ByteString.Lazy.Char8 as STR (pack,cons,unlines)

transform3 :: [Word8] -> [Word8]
transform3 [first]                = transform3 [first,0,0]
transform3 [first, second]        = transform3 [first,second,0]
transform3 [first, second, third] =
    let firstOut  = shiftR first 2
        secondOut = (shiftL (first .&. 0x03) 4) + (shiftR second 4)
        thirdOut  = (shiftL (second .&. 0x0f) 2) + (shiftR third 6)
        fourthOut = third .&. 0x3F
    in map (+32) [firstOut,secondOut,thirdOut,fourthOut]

partitionBSEach :: Int -> L.ByteString -> [L.ByteString]
partitionBSEach n xs
    | xs == L.empty = []
    | otherwise     = let (this,rest) = L.splitAt (fromIntegral n) xs
                      in this : partitionBSEach n rest

shiftBytes :: L.ByteString -> L.ByteString
shiftBytes = L.concat . map (L.pack . transform3 . L.unpack) . partitionBSEach 3


formatString :: L.ByteString -> L.ByteString
formatString xs = STR.unlines [ lnChar x `L.cons` x | x <- partitionBSEach 60 xs ]
    where lnChar = fromIntegral . (+32) . ceiling . (*(3/4)) . fromIntegral . L.length

uuEncode :: String ->  L.ByteString -> L.ByteString
uuEncode fileName xs = header <> (formatString . shiftBytes) xs <> footer
    where header = "begin 644 " <> STR.pack fileName <> "\n"
          footer = "`\nend\n" :: L.ByteString

main = do
        args <- getArgs
        case args of
            [input, output] ->
                               do
                                inFile <- L.readFile input
                                L.writeFile output $ uuEncode input inFile
            [input]         -> do
                                inFile <- L.readFile input
                                L.putStr $ uuEncode input inFile
            []              -> L.interact $ uuEncode "undefined"
            _               -> do
                                pn <- getProgName
                                putStrLn $ "Usage: " ++ pn ++ " [infile] [outfile]"
