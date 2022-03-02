import matplotlib.pyplot as plt
import numpy as np

# plt.style.use('_mpl-gallery')

lines = open('benchmark_results.txt').readlines()[1:]
lines = list(map(lambda s: s.replace('±', '').replace(',', '.').split(), lines))

names = []
y = []
err = []

for line in lines:
    name = line[0]
    if name == 'Simple.simple':
        name = 'Simple'
    if name == 'Simple.streamed':
        name = 'Stream'
    if name == 'Threaded.benchmark':
        name = f'Threaded\n{line[1]} cores'
    names.append(name)
    y.append(float(line[4]))
    err.append(float(line[5]))

print(names, y, err)

x = [i for i in range(len(lines))]

fig, ax = plt.subplots() # type: (plt.Figure, plt.Axes)
ax.get_yaxis().set_label('time, seconds')
ax.bar(names, y, yerr=err, capsize=5)

# bar

plt.show()