class Question:
    def __init__(self, text):
        self.text = text
        self.answers = ""
        self.correct = False
    #print question information to console
    def toString(self):
        print("Question text: ", self.text)
        if(self.answers != ""):
            print("Answer: ",  self.answers)
        print("Correct: " , self.correct)
    #set question's answer
    def setAnswer(self,answer):
        self.answers = answer
    #get question
    def getText(self):
        return self.text