# Read answer keys, student list and report files

from Student import *
from Question import *
from xlrd import open_workbook
from AnswerKey import *
from Poll import *

import csv


class ReadFile:
    def __init__(self):
        self.students = []
        self.allPolls = []

    # Read answer key file
    def readAnswerKey(self, fileName):
        with open(fileName, 'r', encoding='utf-8') as file:
            keys = []
            questions = []
            reader = csv.reader(file)
            poll_name = ""
            is_first_line = True
            for row in reader:
                # Set the name of the poll
                if is_first_line:
                    poll_name = row[0]
                    is_first_line = False
                else:
                    # Get question text and create Question
                    questionKey = Question("".join(row[0].splitlines()))  # for question with correct answers
                    question = Question(row[0])  # to add the question to the poll without any answers given
                    questions.append(question)

                    # Create an answer key for the poll
                    answer_key = AnswerKey(poll_name, questionKey, row[1: len(row)])
                    answer_key.trace()
                    # Keep the answer keys
                    keys.append(answer_key)

            if len(questions) != 0:
                if len(self.allPolls) == 0:
                    poll = Poll(poll_name, keys, questions)
                    poll.trace()
                    self.allPolls.append(poll)

                elif self.allPolls[0].pollName != answer_key.poll_name:
                    poll = Poll(poll_name, keys, questions)
                    poll.trace()
                    self.allPolls.append(poll)


    # Read student list
    def readStudentFile(self, fileName):
        wb = open_workbook(fileName)
        for sheet in wb.sheets():
            number_of_rows = sheet.nrows
            number_of_columns = sheet.ncols
            for row in range(1, number_of_rows):
                values = []
                for col in range(1, number_of_columns):
                    value = sheet.cell(row, col).value
                    if value != '':
                        values.append(value)
                if values and type(values[0]) == float:
                    # Create student
                    if len(values) == 5:
                        student = Student(values[1], values[2], values[3], values[4])
                    else:
                        # if the description is empty
                        student = Student(values[1], values[2], values[3], '')
                    student.trace()
                    # Keep the students
                    self.students.append(student)
        return self.students


    # Read the report file
    def readReportFile(self, fileName,usedPolls):
        number = 0
        with open(fileName, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            poll1 = Poll("poll1", [], [])
            for row in reader:
                questions = []
                for i in range(4, len(row) - 1):
                    if i % 2 == 0:
                        # Create a question and add the answer
                        question = Question("".join(row[i].splitlines()))
                        question.setAnswer(row[i + 1])
                        questions.append(question)
                        poll1.questions.append(question)

                # find which student, and which poll then create that poll and add the student's polls
                for student in self.students:
                    if student.checkStudent(row[1]):
                        for poll in self.allPolls:
                            if poll.checkPoll(questions):

                                if poll.answerKeys[0].used==False:
                                    temp_poll=Poll(poll.pollName,poll.answerKeys,poll.questions)
                                    usedPolls.append(temp_poll)
                                count=0
                                for poll in usedPolls:
                                    if poll.pollName==usedPolls[len(usedPolls) - 1].pollName:
                                        count=count+1
                                if poll.answerKeys[0].used == False:
                                    if count > 1:
                                        usedPolls[len(usedPolls)-1].pollName=usedPolls[len(usedPolls)-1].pollName+"-"+str(count)
                                poll.answerKeys[0].used=True

                                number = number + 1
                                temp = Poll(temp_poll.pollName,temp_poll.answerKeys,temp_poll.questions)
                                temp.questions = poll1.questions
                                temp.setDate(row[3])
                                student.addPoll(temp)

                poll1.questions = []
            for poll in self.allPolls:
                poll.answerKeys[0].used=False
