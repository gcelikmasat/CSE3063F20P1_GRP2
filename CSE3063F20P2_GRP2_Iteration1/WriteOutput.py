import os
from xlrd import open_workbook
from xlwt import Workbook
import pandas as pd

class WriteOutput:

    def __init__(self, students,usedPolls):
        self.students = students
        self.usedPolls = usedPolls

    #Writes results of poll for each student to .xls file.
    def writeGlobalResult(self):
        #Executes for each poll.
        for printPoll in self.usedPolls:
            #Executes if it is not attendance Quiz
            if "Attendance" not in printPoll.pollName:
                wb = Workbook()
                sheet1 = wb.add_sheet('Sheet 1')
                row = 0

                #Writes question to first row
                for column in range(len(printPoll.questions)):
                    sheet1.write(row, column + 1, printPoll.questions[column].text)
                #Writes all students first name and last name
                for student in self.students:
                    row = row + 1
                    sheet1.write(row, 0, student.FirstName + " " + student.LastName)
                    #Checks all student's poll.
                    for check in range(len(student.polls)):
                        if row == 1 and check == 0:
                            sheet1.write(row - 1, len(student.polls[check].questions) + 1, "Number of Questions")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 2, "Success Rate")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 3, "Success Percentage")

                        if printPoll.pollName == student.polls[check].pollName:
                            for column in range(len(student.polls[check].questions)):
                                #If answer is correct it prints 1 otherwise it prints 0.
                                if student.polls[check].questions[column].correct == True:
                                    sheet1.write(row, column + 1, '1')
                                else:
                                    sheet1.write(row, column + 1, '0')
                            #Prints total question, true answers and success rate.
                            sheet1.write(row, len(student.polls[check].questions) + 1,
                                         len(student.polls[check].questions))
                            sheet1.write(row, len(student.polls[check].questions) + 2,
                                         "Total: " + str(len(student.polls[check].questions)) + " Correct: " + str(
                                             student.polls[check].calculateCorrect()))
                            sheet1.write(row, len(student.polls[check].questions) + 3, "%" + str(
                                student.polls[check].calculateCorrect() / len(student.polls[check].questions)))
                wb.save(str(printPoll.pollName) + ".xls")



    #Writes attandance information for each student to .xlsx file
    def writeAttandence(self,metric):
        studentattandence = metric.calculateAttandance(self.usedPolls)
        data = pd.DataFrame(studentattandence,columns=['name', 'surname', 'attended clases', 'total classes', 'attendance percentage'])
        data.to_excel('AttendanceAll.xlsx')
