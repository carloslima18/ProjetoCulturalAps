<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Publicacaouser;

/**
 * PublicacaouserSearch represents the model behind the search form of `app\models\Publicacaouser`.
 */
class PublicacaouserSearch extends Publicacaouser
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id'], 'integer'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'aprovado', 'geo_gps', 'img1', 'img2', 'img3', 'img4', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5'], 'safe'],
            [['latitude', 'longitude'], 'number'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function scenarios()
    {
        // bypass scenarios() implementation in the parent class
        return Model::scenarios();
    }

    /**
     * Creates data provider instance with search query applied
     *
     * @param array $params
     *
     * @return ActiveDataProvider
     */
    public function search($params)
    {
        $query = Publicacaouser::find();

        // add conditions that should always apply here

        $dataProvider = new ActiveDataProvider([
            'query' => $query,
        ]);

        $this->load($params);

        if (!$this->validate()) {
            // uncomment the following line if you do not want to return any records when validation fails
            // $query->where('0=1');
            return $dataProvider;
        }

        // grid filtering conditions
        $query->andFilterWhere([
            'id' => $this->id,
            'latitude' => $this->latitude,
            'longitude' => $this->longitude,
        ]);

        $query->andFilterWhere(['ilike', 'nome', $this->nome])
            ->andFilterWhere(['ilike', 'redesocial', $this->redesocial])
            ->andFilterWhere(['ilike', 'endereco', $this->endereco])
            ->andFilterWhere(['ilike', 'contato', $this->contato])
            ->andFilterWhere(['ilike', 'email', $this->email])
            ->andFilterWhere(['ilike', 'atvexercida', $this->atvexercida])
            ->andFilterWhere(['ilike', 'categoria', $this->categoria])
            ->andFilterWhere(['ilike', 'aprovado', $this->aprovado])
            ->andFilterWhere(['ilike', 'geo_gps', $this->geo_gps])
            ->andFilterWhere(['ilike', 'img1', $this->img1])
            ->andFilterWhere(['ilike', 'img2', $this->img2])
            ->andFilterWhere(['ilike', 'img3', $this->img3])
            ->andFilterWhere(['ilike', 'img4', $this->img4])
            ->andFilterWhere(['ilike', 'campo1', $this->campo1])
            ->andFilterWhere(['ilike', 'campo2', $this->campo2])
            ->andFilterWhere(['ilike', 'campo3', $this->campo3])
            ->andFilterWhere(['ilike', 'campo4', $this->campo4])
            ->andFilterWhere(['ilike', 'campo5', $this->campo5]);

        return $dataProvider;
    }
}
