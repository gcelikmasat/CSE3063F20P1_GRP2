# Read answer keys, student list and report files
import json
import re

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
        keys = []
        questions = []
        poll_name = ""
        questionKey = Question("")
        correct_answers = []

        with open("Answer/"+fileName) as fp:
            for line in fp:
                if line.find("Poll") != -1:
                    # print("name of the poll")
                    poll = Poll(poll_name, keys, questions)
                    poll.trace()
                    self.allPolls.append(poll)
                    poll_name = re.split(r'\t+', line.rstrip('\t'))
                    poll_name = poll_name[0].replace(':', '').replace(' ', '_')
                    poll_name = poll_name[1: len(poll_name)]
                    questions = []

                if line[0].isnumeric():
                    # Create an answer key for the poll
                    answer_key = AnswerKey(poll_name, questionKey, correct_answers)
                    answer_key.trace()
                    # Keep the answer keys
                    keys.append(answer_key)
                    correct_answers = []
                    # Get question text and create Question
                    questionKey = Question(line.split('. ')[1].replace(" ( Multiple Choice)", '').replace(" ( Single Choice)", '').strip())  # for question with correct answers
                    question = Question(
                        line.split('. ')[1].replace(" ( Multiple Choice)", '').replace(" ( Single Choice)", '').strip())  # to add the question to the poll without any answers given
                    questions.append(question)

                while line.find("Answer") != -1:
                    correct_answers.append(line.split(': ')[1].strip())
                    line = fp.readline()


        fp.close()

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
    def readReportFile(self, fileName, usedPolls):
        number = 0
        with open("Quiz/"+fileName, 'r', encoding='utf-8') as file:
            reader = csv.reader(file)
            poll1 = Poll("poll1", [], [])
            anomalies = []
            for row in reader:

                if row[0].isnumeric():

                    questions = []

                    for i in range(4, len(row) - 1):
                        if i % 2 == 0:
                            # Create a question and add the answer
                            question = Question("".join(row[i].splitlines()))
                            question.setAnswer(row[i + 1])
                            questions.append(question)
                            poll1.questions.append(question)
                    checked = False
                    # find which student, and which poll then create that poll and add the student's polls
                    for student in self.students:

                        if student.checkStudent(row[1]):
                            checked = True
                            for poll in self.allPolls:
                                if poll.checkPoll(questions):

                                    if not poll.answerKeys[0].used:
                                        temp_poll = Poll(poll.pollName, poll.answerKeys, poll.questions)
                                        usedPolls.append(temp_poll)
                                    count = 0
                                    for poll in usedPolls:
                                        if poll.pollName == usedPolls[len(usedPolls) - 1].pollName:
                                            count = count + 1
                                    if not poll.answerKeys[0].used:
                                        if count > 1:
                                            usedPolls[len(usedPolls) - 1].pollName = usedPolls[len(
                                                usedPolls) - 1].pollName + "-" + str(count)
                                    poll.answerKeys[0].used = True

                                    number = number + 1
                                    temp = Poll(temp_poll.pollName, temp_poll.answerKeys, temp_poll.questions)
                                    temp.questions = poll1.questions
                                    temp.setDate(row[3])
                                    student.addPoll(temp)

                    poll1.questions = []
                    if (checked == False):
                        anomalies.append(row[1])

            for poll in self.allPolls:
                poll.answerKeys[0].used = False

        with open('anomalies.json', 'w', encoding='utf-8') as f:
            json.dump(anomalies, f, ensure_ascii=False, indent=4)