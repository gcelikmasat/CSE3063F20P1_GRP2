## a poll contains number of questions and a answerkey to those question.
## this class is used for poll statistic for each student on a specific day.

import logging

class Poll:

    def __init__(self, pollName,answerKeys,questions):
        self.pollName = pollName
        self.questions = questions
        self.student = []
        self.answerKeys = answerKeys
        self.date = ""

    #information trace
    def trace(self):
        message = " Poll created: " + self.pollName + " created"
        logging.info(message)


    def toString(self):
        print("-------Poll-----")
        print("Poll name: ", self.pollName)
        print("Poll date: ", self.date)
        print("Total: ", len(self.questions)," Correct", str(self   .calculateCorrect()))
        #poll's questions
        if(len(self.questions) != 0):
            for question in self.questions:
                question.toString()

    # checks that if the poll contain given question inside
    def isInside(self,tempQuestion):
        for question in self.questions:
            if(question.text == tempQuestion.text):
                return True

    #checks that given question list is the same questions in this poll, and this means poll is found.
    def checkPoll(self,questionList):

        count = 0       #counts checked questions
        lenghtList = len(questionList)
        lengthQuestionPoll = len(self.questions)
        #if given question list is already different than poll's questions size, than this means it is not the same poll.
        if(lenghtList != lengthQuestionPoll):
            return False;
        elif(lenghtList == lengthQuestionPoll):
            for list in questionList:
                if(self.isInside(list)):
                    count = count + 1
                if(lengthQuestionPoll==1):
                    count=count-1
            if(count+1 == lengthQuestionPoll):
                return True
    #sets date
    def setDate(self, date):
        self.date = date

    #calculates how many correct answers does this poll has.
    def calculateCorrect(self):
        count = 0
        for question in self.questions:
            if (question.correct == True):
                count = count + 1
        return count

    #finds the given question in this poll and returns it
    def findAndGetQuestion(self, question):
        for q in self.questions:
            if (q.text == question.text):
                return q

    #finds the question and return its answers
    def getQuestionAnswer(self, question):
        for answerKey in self.answerKeys:
            if answerKey.question.text == question.text:
                return answerKey.question.answers


