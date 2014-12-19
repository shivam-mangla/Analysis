import numpy as np
import csv
from numpy import array, random
from scipy.cluster.vq import vq, kmeans, whiten

fl = open('SentiWordNet.csv', 'r');
csvReader = csv.Reader(fl, delimiter = ',');
features = np.asarray(list(csvReader));

features_n = [];
for feature in features:
	fea = [float(feature[0]), float(feature[1])]
	features_n.append(fea)

features = np.asarray(list(features_n));
whitened = whiten(features)

book = array((whitened[0],whitened[2]))
kmeans(whitened,book)

random.seed((1000,2000))
codes = 5
kmeans(whitened,codes)