from PIL import Image, ImageDraw, ImageFont
import qrcode
import csv

MAX_LINE_LEN = 28

f = open('final.csv')
csv_reader = csv.reader(f, delimiter='|')

allcodes = []
descriptions = []

for row in csv_reader:
    allcodes.append(row[0])
    descriptions.append(row[1])

    code = row[0]
    str = row[1]
    img = Image.new('RGB', (1000, 360), color=(255, 255, 255))
    img2 = qrcode.make(code)
    fnt = ImageFont.truetype("Helvetica Bold.ttf", 60)
    fnt2 = ImageFont.truetype("Helvetica.otf", 50)

    if (len(str) > MAX_LINE_LEN):
        if (len(str) > MAX_LINE_LEN * 2):

            str1 = str[0:28].rfind(" ") + 1
            str2 = str1 + str[str1:str1 + MAX_LINE_LEN].rfind(" ") + 1
            str3 = 0
            if (len(str) > str2 + MAX_LINE_LEN):
                str3 = str2 + MAX_LINE_LEN
            else:
                str3 = len(str)

            d = ImageDraw.Draw(img)
            d.text((30, 50), code, font=fnt, fill=(0, 0, 0))
            d2 = ImageDraw.Draw(img)
            d2.text((30, 140), str[0:str1], font=fnt2, fill=(0, 0, 0))
            d2.text((32, 200), str[str1:str2], font=fnt2, fill=(0, 0, 0))
            d2.text((32, 260), str[str2:str3], font=fnt2, fill=(0, 0, 0))

        else:
            str1 = str[0:28].rfind(" ") + 1
            str2 = 0
            if (len(str) > str1 + MAX_LINE_LEN):
                str2 = str1 + MAX_LINE_LEN
            else:
                str2 = len(str)

            d = ImageDraw.Draw(img)
            d.text((30, 80), code, font=fnt, fill=(0, 0, 0))
            d2 = ImageDraw.Draw(img)
            d2.text((30, 170), str[0:str1], font=fnt2, fill=(0, 0, 0))
            d2.text((32, 230), str[str1:str2], font=fnt2, fill=(0, 0, 0))
    else:
        d = ImageDraw.Draw(img)
        d.text((30, 110), code, font=fnt, fill=(0, 0, 0))
        d2 = ImageDraw.Draw(img)
        d2.text((30, 200), str, font=fnt2, fill=(0, 0, 0))

    img.paste(img2, (700, 40))
    newstr = "".join([c for c in code if c.isalpha() or c.isdigit() or c == ' ']).rstrip()
    img.save('labels/'+newstr+'.png')

print(max(allcodes, key=len))
print(max(descriptions, key=len))
print(len("Superior Industries Lablesns")) #28


# 51 x 197


# img2 = qrcode.make("CCPP7")
# fnt = ImageFont.truetype("Helvetica Bold.ttf",60)
# fnt2 = ImageFont.truetype("Helvetica.otf", 50)
# d = ImageDraw.Draw(img)
# d.text((30, 50), "1XPRINTING-COMBRD", font=fnt, fill=(0, 0, 0))
# d2 = ImageDraw.Draw(img)
# d2.text((30,140), "Superior Industries Lablesns", font = fnt2, fill=(0,0,0))
# d2.text((32,200), "100X60 Extra sticky (XPG) ", font=fnt2, fill=(0,0,0))
# d2.text((32,260), "40mm cores 500/roll", font=fnt2, fill=(0,0,0))
# img.paste(img2, (700, 40))
# img.save('helvetica.png')


print('complete')