# ClassPicker
Helper to help me arrange class schedule

## Input File explanation:
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
    - useless because i use subject code ha ha ha
  - SubjectCode
    - subject code, I allowed same code but not recommend because it will make u confused when output because I show with subject code and not subject name
    - input with 1 space
  - Required
    - input number of class needed for each group (group is used to separate Lecture, tutorial and Lab class)
    - split with 4 space
    - e.x:    A-1    B-2 (mean need 1 from group A, 2 from group B)
  - SubjectTime:
    - used to input classes
    - split with 4 space for each data, start with group, then class code, which day, start time, end time, location
  - Chained
    - used to input with class must come with other class
    - split with 4 space
    - e.x:    TC02-TT02,TT02/TT02,TT03 (if u take TC02, it must have 2 TT02 class or TT02 and TT03)
  - Preferred
    - used to input prefer which class
    - split with 4 space
    - e.x:    TC01    TC02
  - end
    - end when subject is done input and need to input new subject
    
## Output File explanation:
  - menu.csv
    - record feature of each possible schedule
    - below explain about column
    - file id
      - file name that target
    - days
      - which day need to study
      - e.x: 4=[1, 3, 4, 5] mean 4 day to study, Monday, Wednesday, Thursday, Friday
    - duration
      - didn't do because of logic problem(im stupid இ௰இ)
      - first class and last class of week
      - if study 1-5, then take monday first class and friday last class, but wat if study 6-3, I should take saturday first class and wednesday last class, but hard to achieve
      - and if someone studies 5,7,1,2,3 then more fuck up
    - start times, end times
      - start time and end time for every day, arrange according days
      - it shows with index of above's list
      - it is because we can see more easily for which schedule having the earliest class and latest class
      - 0 is earliest for start time and latest for end times, bigger mean later for start time and ...
    - n of classes
      - show how many class for every day, arrange according days
    - n of prefer class
      - how many prefer class have been picked
    - subjects
      - subject list
      - useful when having more optional subject
    - study percentage
      - show how many times u study between first class of day till last class of day
      - have been sum and become percentage
      - formula = (total study time every day /(end time of last class every day-start time of first class every day)).sum()/number of day study

## Thing Can update:
  - Let combination function become T (dynamic type function)
  - Combination function should be list A * list B, not list A only
    - different is if list a * b will be [[A1,B1],[A2,B2],...], but list a will become [[A1A1],[A1A2]]
    - it can reduce the processing on my code

but I have final to study and other project I want to do, so... can run jiu ok lah ha ha ha ha
