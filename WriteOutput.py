import os
from xlwt import Workbook
import pandas as pd
import logging


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
                sheet1 = wb.add_sheet('Sheet 1', cell_overwrite_ok=True)
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
                            sheet1.write(row - 1, len(student.polls[check].questions) + 2, "Number of Correctly Answered Questions")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 3, "Number of Wrongly Answered Questions")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 4, "Number of Empty Questions")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 5, "Rate of Correctly Answered Questions")
                            sheet1.write(row - 1, len(student.polls[check].questions) + 6, "Accuracy Percentage")

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
                            sheet1.write(row, len(student.polls[check].questions) + 2, str(student.polls[check].calculateCorrect()))
                            sheet1.write(row, len(student.polls[check].questions) + 3, str
                            (len(student.polls[check].questions) - student.polls[check].calculateCorrect()))
                            sheet1.write(row, len(student.polls[check].questions) + 4, str('0'))
                            sheet1.write(row, len(student.polls[check].questions) + 5, str(
                                student.polls[check].calculateCorrect() / len(student.polls[check].questions)))
                            sheet1.write(row, len(student.polls[check].questions) + 6, "%" + str(
                                student.polls[check].calculateCorrect() / len(student.polls[check].questions) * 100))



                filename_str = (str(printPoll.pollName).replace("\t", "").replace("\n", "").replace(" ", "_"))
                wb.save(os.getcwd() + "/Result/"+filename_str + ".xls")


    def writeStudentResult(self):

        for student in self.students:

            for poll in student.polls:
                wb = Workbook()
                sheet1 = wb.add_sheet('Sheet 1', cell_overwrite_ok=True)


                sheet1.write(0,  0, "Question Text")
                sheet1.write(0,  1, "Given Answer")
                sheet1.write(0,  2, "Correct Answer")
                sheet1.write(0,  3, "Correct")

                for row in range(len(poll.questions)):
                    sheet1.write(row+1, 0, poll.questions[row].text)
                    sheet1.write(row+1, 1, poll.questions[row].answers)

                    for question_key in poll.answerKeys:
                        if poll.questions[row].text in question_key.question.text or question_key.question.text in poll.questions[row].text :
                            sheet1.write(row+1, 2, question_key.question.answers)


                    if(poll.questions[row].correct):
                        sheet1.write(row+1, 3, "1")
                    else:
                        sheet1.write(row+1, 3, "0")

                message = poll.pollName + " output for student " + student.FirstName + " " + student.LastName + " is created"
                logging.info(message)
                filename_str = (str(poll.pollName).replace("\t", "").replace("\n", "").replace(" ", "_")+str(poll.date).replace(", ","_").replace(":","_").replace(" ","_")+"_"+str(student.FirstName).replace(" ","_")+"_"+str(student.LastName))+"_"+str(student.id)
                wb.save(os.getcwd() + "/Result/Students/"+filename_str + ".xls")



    #Writes attandance information for each student to .xlsx file
    def writeAttandence(self,metric):
        studentattandence = metric.calculateAttandance(self.usedPolls)
        data = pd.DataFrame(studentattandence,columns=['name', 'surname', 'attended clases', 'total classes', 'attendance percentage'])
        data.to_excel('AttendanceAll.xlsx')
