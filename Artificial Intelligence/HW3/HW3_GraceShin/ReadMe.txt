1. Lucas Shin & Sam Grace

2. Required Files: DecisionTree.java, Example.java, TestClassifier.java, TestClassifierHepatitis.java, TreeNode.java

3. None

4. 
    Part 2(a):
        set1 small: 
            Positive examples correct: 10 out of 10
            Negative examples correct: 10 out of 10
        set 2 small:
            Positive examples correct: 6 out of 10
            Negative examples correct: 10 out of 10
        set 1 big:
            Positive examples correct: 100 out of 100
            Negative examples correct: 100 out of 100
        set 2 big:
            Positive examples correct: 95 out of 100
            Negative examples correct: 95 out of 100
        
        Analysis: Focusing on the big data sets, my decision tree does extremely well (100% correct) for set 1 big and relatively 
        well for set 2 big (95% correct). We know that set 2 contains some "noisy" data, meaning that positive training examples 
        always have the property but about 10% of negative training examples also do. Therefore, it seems that our decision trees 
        perform slightly worse when tested on noisy data. We assume this is the case because our decision tree only classifies 
        a small percentage of the negative examples with the property that is contained in all of the positive examples.
        
    Part 2(b):
        (i) In short, the decision tree identifies the first few strong indicators in hepatitis lethality- ascites, sgot levels, and age. 
        If a patient is a child, they have a high chance of survival, as it requires both forgoing antiviral medication and also 
        exhibiting a large number of serious symptoms.
        
        The first choice the decision makes on patients relies on the presence of ascites- fluid buildup in the abdomen- meaning 
        this is a large separator of lethality in hepatitis. If it's absent, the only other feature that matters is if the patient's 
        albumin levels are below 3.85, which would make the case classified as lethal. If ascites is present, we then classify 
        patients by their sgot levels. If it's between 60 and 150, then we check the firmness of the liver. If the liver is firm and 
        the patient is also experiencing fatigue, they are considered not likely to die. However, if they are experiencing fatigue and 
        outside the age range of 31-50, they are considered to have a lethal case. If the liver isn't firm, then that immediately 
        classifies the case as lethal. Backtracking to sgot levels, if they are outside 60-156, we then check for varices, the 
        swelling of blood vessels, especially in the esophagus. If the patient doesn't have varices or general discomfort/unwellness, 
        they're expected to survive. If they are experiencing malaise, then it's expected to be lethal. If the patient does have 
        varices, then they're checked if they're younger than 30. Those younger than 30 are expected to survive with the usage of 
        antivirals. If antivirals aren't used, then the patient is again checked for malaise. If they're experiencing that, have a 
        firm liver, palpable spleen, bilirubin level above 2.9, and an sgot between 60-156, the patient is expected to die. If their 
        spleen isn't palpable but is large, they're also expected to die.

        (ii) Correctly classified examples: 46 (out of 52)
            # of false positives: 3 (out of 10 negative examples)
            # of false negatives: 3 (out of 42 positive examples)

    Part 3:
        (a) Symptoms and COVID Presence (May 2020 data)
        (b) Kaggle (https://www.kaggle.com/datasets/hemanthhari/symptoms-and-covid-presence)
        (c) This data set contains various symptoms' presence (yes or no) and classifies the person as testing positive for COVID-19
            or not (we are predicting the presence of Covid-19). The attributes are breathing problem, fever, dry cough, sore throat, 
            runny nose, asthma, chronic lung disease, headache, heart disease, diabetes, hyper tension, fatigue, gastrointestinal,
            abroad travel, contact with COVID patient, attended large gathering, visited public exposed places, family working in
            public exposed places, wearing masks, and sanitization from market. 
        (d) There are 5,434 examples (people).
        (e) Each example has 21 features including the variable being predicted (COVID-19 or not).

        We split our data into a training set of 1,981 randomly selected examples and a testing set of the remaining 3,453 examples.
        Out of the 3,453 testing examples, 3,030 were positive examples (people with COVID), while 423 were negative examples. Our decision
        tree performed quite well on the testing data, with 3,176 predictions correct out of the 3,453 total testing examples (just under 
        92%). Below is a breakdown of the results:
            Correctly classified examples: 3,176 (out of 3,453), which is 92% accurate
            # of false positives: 110 (out of 423 negative examples)
            # of false negatives: 167 (out of 3,030 positive examples)

