# ClassPicker
Helper to help me arrange class schedule

## Input File explaination:
  - MustTake
    - input subject code that must take
    - split subject code with 4 space
    - e.x:    qwe    ABC    XYZ    123
  - OptionalNeeded
    - input number of subject that needed, will get from subject not include from MustTake
    - input with 1 space
    - e.x: 3
    
  - Subject
    - subject name
    - input with 1 space
    - useless because i use subject code hahahahahah
  - SubjectCode
    - subject code, i allowed same code but not tuijian because it will make u confuss when output because i show with subject code and not subject name
    - input with 1 space
  - Required
    - input number of class needed for each group (group is use to seperate Lecture, tutorial and Lab class)
    - split with 4 space
    - e.x:    A-1    B-2 (mean need 1 from group A, 2 from group B)
  - SubjectTime:
    - use to input classes
    - split with 4 space for each data, start with group, then class code, which day, start time, end time, location
  - Chained
    - use to input with class must come with other class
    - split with 4 space
    - e.x:    TC02-TT02,TT02/TT02,TT03 (if u take TC02, it must have 2 TT02 class or TT02 and TT03)
  - Preferred
    - use to input prefer which class
    - split with 4 space
    - e.x:    TC01    TC02
  - end
    - end when subject is done input and need to input new subject
    
## Output File explaination:
  - menu.csv
    - record feature of each possible schdule
    - below explain about column
    - file Id
      - file name that target
    - days
      - which day need to sudy
      - e.x: 4=[1, 3, 4, 5] mean 4 day to study, monday, wednesday, thusday, friday
    - duration
      - didn't do because of logic problem(im stupid இ௰இ)
      - first class and last class of week
      - if study 1-5, then take monday first class and friday last class, but wat if study 6-3, i should take saturday first class and wednesday last class, but hard to achieve
      - and if someone study 5,7,1,2,3 then more fuck up
    - start times, end times
      - start time and end time for everyday, arrange according days
      - it show with index of above's list
      - it is because we can see more easily for which schdule having earliest class and latest class
      - 0 is earilest for start time and lastest for end times, bigger mean later for start time and ...
    - n of classes
      - show how many class for every day, arrange according days
    - n of prefer class
      - how many prefer class have been pick
    - subjects
      - subject list
      - useful when haveing more optional subject
    - study percentage
      - show how many time u study between first class of day till last class of day
      - have been sum and become percentage
      - formula = (total study time for every day/(end time of last class every day-start time of first class every day)).sum()/number of day study

### Thing Can update:
  - Let combination function become T (dynamic type function)
  - Combination function should be list a * list b, not list a only
    - different is if list a * b will be [[a1,b1],[a2,b2],...], but list a will become [[a1a1],[a1a2]]
    - it can reduce the processing on my code

but i have final to study and other project i want to do, so... can run jiu ok lah hahahaha
