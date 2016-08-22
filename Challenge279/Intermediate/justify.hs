import Data.List.Split (splitOn)
import System.Environment (getArgs, getProgName)
import Data.List (intersperse)
import Data.Monoid ((<>))

main = do args <- getArgs
          case args of [n] -> let readResult = reads n :: [(Int,String)]
                              in case readResult of
                                    [(lineWidth,_)] -> interact $ justify lineWidth
                                    _               -> displayUsage
                       _   -> displayUsage
    where displayUsage = do pn <- getProgName
                            putStrLn $ "Usage: " ++ pn ++ " [# of chars/line]"

justify :: Int -> String -> String
justify n = unlines . map (padStrTo n) . lines . smartWrap n

smartWrap :: Int -> String -> String
smartWrap lineLen str = reJoin $ smartWrapHelper (keepParagraphWords str) 0
    where smartWrapHelper (x:xs) sinceNL
                | ('\n':_) <- x          = x : smartWrapHelper xs 0
                | otherwise              = let curLen = length x
                                               totLen = curLen + sinceNL
                                           in case compare totLen lineLen of
                                                LT      -> x : smartWrapHelper xs (totLen + 1)
                                                GT      -> (if sinceNL > 0
                                                             then '\n':x
                                                             else x)
                                                           : smartWrapHelper xs (curLen + 1)
                                                EQ      -> (x ++ "\n") : smartWrapHelper xs 0
          smartWrapHelper [] _ = []
          
          specialConcat :: (String -> String -> String) -> [String] -> String
          specialConcat f (x:xs) = let already = specialConcat f xs
                                   in (f x already) ++ already
          specialConcat _ [] = []
          
          reJoin :: [String] -> String
          reJoin = specialConcat $ \x thusFar ->
                                        if null thusFar
                                         then x
                                         else case head thusFar of
                                            '\n' -> x
                                            _    -> if ((not . all (=='\n')) x) && (last x /= '\n')
                                                     then x ++ " "
                                                     else x
    
          keepParagraphWords :: String -> [String]
          keepParagraphWords = filter (not . null) . splitOn " " . keepParagraphs
              where keepParagraphs :: String -> String
                    keepParagraphs ('\n':xs)
                          | ('\n':_) <- xs   = ' ' : '\n' : paragMode xs
                          | otherwise        = ' ' : keepParagraphs xs
                          where paragMode ('\n':xs) = '\n' : paragMode xs
                                paragMode (x:xs)    = ' ' : x : keepParagraphs xs
                    keepParagraphs (x:xs) = x : keepParagraphs xs
                    keepParagraphs [] = []

padStrTo :: Int -> String -> String
padStrTo _ [] = []
padStrTo n xs = let xsLength = length xs
                    toAdd = n - xsLength
                in case toAdd <= 0 || xsLength < toAdd of
                    True    -> xs
                    False   -> let (half1,half2)     = splitOnSpaceNearestCenter xs
                               in case null half2 of
                                    True -> xs
                                    _ -> let (center:half1MiddleOut) = reverse $ splitOn " " half1
                                             half2MiddleOut        = splitOn " " half2
                                             (half1',half2')       = padHalves toAdd half1MiddleOut half2MiddleOut
                                         in (concat . intersperse " ") $ (reverse half1') <> (center:half2')
    where padHalves 0 h1 h2 = (h1,h2)
          padHalves remaining h1 h2
            | null h1               = ([], padRight (calculatePasses remaining h2Length) remaining h2)
            | otherwise             = let halfOfRem     = fromIntegral remaining / 2
                                          (h1Pad,h2Pad) = (floor halfOfRem, ceiling halfOfRem)
                                          h1Passes      = calculatePasses h1Pad h1Length
                                          h2Passes      = calculatePasses h2Pad h2Length
                                      in (padLeft h1Passes h1Pad h1 , padRight h2Passes h2Pad h2)
            where h1Length = length h1
                  h2Length = length h2
                  
                  calculatePasses pad len = ceiling $ (fromIntegral pad) / (fromIntegral len)
                  
                  padRight rpasses remaining h2 = if rpasses <= 1
                                                    then padRightHelper remaining h2
                                                    else padRight (rpasses - 1) (remaining - h2Length) $ padRightHelper remaining h2
                  
                  padRightHelper 0 h2 = h2
                  padRightHelper remaining [] = []
                  padRightHelper remaining (h2H:h2Rest) = (' ':h2H) : padRightHelper (remaining - 1) h2Rest

                  padLeft rpasses remaining h1 = if rpasses <= 1
                                                   then padLeftHelper remaining h1
                                                   else padLeft (rpasses - 1) (remaining - h1Length) $ padLeftHelper remaining h1
                  
                  padLeftHelper 0 h1 = h1
                  padLeftHelper remaining [] = []
                  padLeftHelper remaining (h1H:h1Rest) = (h1H ++ " ") : padLeftHelper (remaining - 1) h1Rest


splitOnSpaceNearestCenter :: String -> (String, String)
splitOnSpaceNearestCenter xs = let center = ceiling $ (fromIntegral (length xs)) / 2
                               in splitOnSpaceNearest center xs
                               
splitOnSpaceNearest :: Int -> String -> (String, String)
splitOnSpaceNearest = splitOnSubNearest " "

splitOnSubNearest :: String -> Int -> String -> (String, String)
splitOnSubNearest sub n xs = let (h1,h2) = splitOnSNHelper (length sub) 0 ([] , splitOn sub xs)
                                 cleanUp = concat . intersperse sub
                             in (cleanUp h1, cleanUp h2)
    where splitOnSNHelper _ _ (half1,[]) = (half1,[])
          splitOnSNHelper _ _ (half1,[half2])
            | null half1    = ([half2], half1)
            | otherwise     = (half1, [half2])
          splitOnSNHelper subLen curH1Len (half1,half2) =
                let nextH1Len = length (head half2) + subLen + curH1Len
                in case (abs (curH1Len - n)) > (abs (nextH1Len - n)) of
                    True -> splitOnSNHelper subLen nextH1Len (half1 ++ [(head half2)], tail half2)
                    _    -> case null half1 of
                             True -> if (not . null) half2
                                      then ([head half2], tail half2)
                                      else ([],[])
                             _    -> (half1, half2)
