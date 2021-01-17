import pandas as pd
import xlsxwriter


class Metric:
    def __init__(self, read):
        self.students = read.students
        self.allPolls = read.allPolls
        # calculateAttendance: claculate attendance of each student and hold
        # student info + calculation in array than return that array.
    def calculateAttandance(self, usedPolls):

        studentattandence = []
        for student in self.students:
            s = [student.FirstName, student.LastName, len(student.polls), len(usedPolls),
                 str(len(student.polls) / len(usedPolls) * 100) + "%"]
            studentattandence.append(s)

        return studentattandence
        ## This method prints informations like Poll Name, Poll date
        ## Student name... of each student into Global file.xlsx
    def calculateMetrics(self, usedPolls):
        count = 1

        list = []
        for i in usedPolls:
            if "Attendance" not in i.pollName:
                list.append("Poll Name for Poll " + str(count))
                list.append("Poll Date for Poll " + str(count))
                list.append("Poll Questions for Poll " + str(count))
                list.append("Success Percentage for Poll " + str(count))
                count += 1

        df = pd.DataFrame(columns=['First Name', 'Last Name'])
        for j in range(count):
            df[list[j]] = ""

        row = 0
        for student in self.students:
            df = df.append({'First Name': student.FirstName, 'Last Name': student.LastName}, ignore_index=True)

            i = 0
            for poll in student.polls:
                if "Attendance" not in poll.pollName:
                    percentage = (poll.calculateCorrect() / len(poll.questions)) * 100

                    rowIndex = df.index[row]
                    df.loc[rowIndex, list[i]] = poll.pollName
                    df.loc[rowIndex, list[i + 1]] = poll.date
                    df.loc[rowIndex, list[i + 2]] = len(poll.questions)
                    df.loc[rowIndex, list[i + 3]] = "%" + str(percentage)
                    i += 4
            row += 1

        df.to_excel('Global File.xlsx')
        ## This method calculates question distributioni answer distribution for each question
        ## Than create histogram and pie chart of this distributions for each question
        ## Than write/print all of them into excel files like: PollName-Question Number.xlsx
    def calculateQuestionDistrubition(self, usedPolls):

        count = []
        answers = []
        uniqueAnswers = []

        for poll in usedPolls:
            x = 1
            for question in poll.questions:
                correctAnswer = poll.getQuestionAnswer(question)

                for student in self.students:
                    poll2 = student.isPollInside(poll)
                    if poll2 is not None:
                        question2 = poll2.findAndGetQuestion(question)
                        if question2 is not None:
                            answers.append(question2.answers)
                            if question2.answers not in uniqueAnswers:
                                uniqueAnswers.append(question2.answers)

                for i in range(len(uniqueAnswers)):
                    c = 0
                    for a in answers:
                        if a == uniqueAnswers[i]:
                            c = c + 1
                    count.append(c)

                workbook = xlsxwriter.Workbook(str(poll.pollName) + "-Q" + str(x) + '.xlsx')
                x = x + 1
                worksheet = workbook.add_worksheet()
                bold = workbook.add_format({'bold': 1})

                # Add the worksheet data that the charts will refer to.
                headings = ['Unique Answers', 'Answers']
                data = [
                    uniqueAnswers,
                    count
                ]
                worksheet.write_row('A1', headings, bold)
                worksheet.write_column('A2', data[0])
                worksheet.write_column('B2', data[1])

                # Create a new chart object.

                chart1 = workbook.add_chart({'type': 'pie'})
                chart2 = workbook.add_chart({'type': 'column'})

                # Configure the series. Note the use of the list syntax to define ranges:

                chart1.add_series({
                    'name': 'Correct answer: ' + str(correctAnswer),
                    'categories': ['Sheet1', 1, 0, len(uniqueAnswers), 0],
                    'values': ['Sheet1', 1, 1, len(uniqueAnswers), 1],

                })
                chart2.add_series({
                    'name': 'Histogram',
                    'categories': ['Sheet1', 1, 0, len(uniqueAnswers), 0],
                    'values': ['Sheet1', 1, 1, len(uniqueAnswers), 1],
                    'gap': 2,
                })

                # Add a title.
                # chart1.set_title({'name': 'Popular Pie Types'})

                # Set an Excel chart style. Colors with white outline and shadow.
                chart1.set_style(10)
                # Insert the chart into the worksheet (with an offset).
                worksheet.insert_chart('C2', chart1, {'x_offset': 40, 'y_offset': 50})
                worksheet.insert_chart('C2', chart2, {'x_offset': 40, 'y_offset': 350})
                workbook.close()

                answers.clear()
                uniqueAnswers.clear()
                count.clear()
