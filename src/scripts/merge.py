import csv 

data = list()

with open('./therest.txt', 'r') as csvfile:
    reader = csv.reader(csvfile, delimiter=" ")

    for i,row in enumerate(reader):
        if i%20 == 0:
            current = list()
            currentB = list()
            number = 0
            numberB = 0
            current = row
        
        if i%20 == 1:
            currentB = row
        
        if i%2 == 0:
            number += float(row[4])
        else:
            numberB += float(row[4])
            print numberB
        
        if i%20 == 19:
            
            current[4] = number/10
            currentB[4] = numberB/10
            data.append(current)
            data.append(currentB)       
            
with open('rest.csv', 'wb') as csvfile:
    csvwriter = csv.writer(csvfile, delimiter=' ')
    csvwriter.writerows(data)