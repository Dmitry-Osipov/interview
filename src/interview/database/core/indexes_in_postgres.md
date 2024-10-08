# Типы индексов в postgres
## Оглавление
- [B-tree](#бинарные-деревья)
- [Hash Indexes](#хеш-индексы)
- [GiST](#gist-индексы)
- [SP-GiST](#sp-gist-индексы)
- [GIN](#gin)
- [BRIN](#brin-индексы)
- [Когда не надо использовать индексы](#когда-не-надо-использовать-индексы)
## Бинарные деревья
B-tree (сбалансированное дерево) - это самый распространённый тип индекса в postgres (стандартный). Он поддерживает все 
стандартные операции сравнения и может использоваться с большинством типов данных. B-tree индексы могут быть 
использованы для сортировки, ограничений уникальности и поиска по диапазону значений.
```sql
CREATE INDEX ix_example_btree ON example_table(column_name);
```
## Хеш-индексы
Хеш-индексы предназначены для обеспечения быстрого доступа к данным по равенству. Они менее эффективны, чем B-tree 
индексы, не поддерживают сортировку или поиск по диапазону значений. Из-за своих ограничений редко используются на 
практике.
```sql
CREATE INDEX ix_example_hash ON example_table USING hash(column_name);
```
## GiST-индексы
SiST-индексы являются обобщёнными и многоцелевыми, предназначены для работы со сложными типами данных, такими как 
геометрические объекты, текст и массивы. Они позволяют быстро выполнять поиск по пространственным, текстовым и 
иерархическим данными.
```sql
CREATE INDEX ix_example_gist ON example_table USING gist(to_tsvector('english', column_name));
```
## SP-GiST-индексы
SP-GiST-индексы предназначены для работы с непересекающимися и неравномерно распределёнными данными. Они эффективны для 
поиска в геометрических и IP-адресных данных.
```sql
CREATE INDEX ix_example_spgist ON example_table spgist (inet(column_name))
```
## GIN
GIN-индексы применяются для полнотекстового поиска и поиска по массивам, JSON и триграммам. Они обеспечивают высокую 
производительность при поиске в больших объёмах данных.
```sql
CREATE INDEX ix_example_gin ON example_table USING gin (to_tsvector('english', column_name));
```
## BRIN-индексы
BRIN-индексы используются для компактного представления больших объёмов данных, особенно когда значения в таблице имеют 
определённый порядок. Они эффективны для хранения и обработки временных рядов и географических данных.
```sql
CREATE INDEX ix_example_brin ON example_table USING brin (column_name);
```
## Когда не надо использовать индексы
- В таблице часто обновляются данные
- Когда в колонке много повторяющихся значений (низкая селективность - это соотношение уникальных значений к общему 
числу значений)
- Когда индекс использует более 3 колонок
- Индексы не должны включаться друг в друга
