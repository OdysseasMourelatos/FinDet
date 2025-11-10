#!/usr/bin/env python
# coding: utf-8


# In[15]:


import pandas as pd
df = pd.read_csv('GovernmentBudgetUpdated.csv')


# In[65]:


print(df.head(5))

# In[4]:


df.dtypes

# In[5]:


df['category']= df['category'].astype(str).str.strip().map({'ΕΣΟΔΑ':1,'ΕΞΟΔΑ':0})
print(df.head(25))

# In[6]:


df.dtypes

# In[7]:


income=0
expenses=0
for index, row in df.iterrows():
  category= row["category"]
  amount= row["amount"]
  if category==0:
    income +=amount
  elif category== 1:
    expenses+=amount
print(income,expenses)

# In[8]:


chart_values=[income,expenses]
print(chart_values)

# In[13]:


import matplotlib.pyplot as plt
labels = ['income', 'expenses']

data = pd.Series(chart_values, index=labels)

data.plot.pie(autopct='%1.1f%%', startangle=90, figsize=(6, 6))
plt.title('income/expenses chart')
plt.ylabel('')
plt.show()
