<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Publicacao;

/**
 * PublicacaoSearch represents the model behind the search form about `app\models\Publicacao`.
 */
class PublicacaoSearch extends Publicacao
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id', 'fk_user'], 'integer'],
            [['nome', 'redesocial', 'endereco', 'contato', 'atvexercida', 'categoria', 'geo_gps', 'img1', 'img2', 'img3', 'img4'], 'safe'],
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
        $query = Publicacao::find();

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
            'fk_user' => $this->fk_user,
        ]);

        $query->andFilterWhere(['like', 'nome', $this->nome])
            ->andFilterWhere(['like', 'redesocial', $this->redesocial])
            ->andFilterWhere(['like', 'endereco', $this->endereco])
            ->andFilterWhere(['like', 'contato', $this->contato])
            ->andFilterWhere(['like', 'atvexercida', $this->atvexercida])
            ->andFilterWhere(['like', 'categoria', $this->categoria])
            ->andFilterWhere(['like', 'geo_gps', $this->geo_gps])
            ->andFilterWhere(['like', 'img1', $this->img1])
            ->andFilterWhere(['like', 'img2', $this->img2])
            ->andFilterWhere(['like', 'img3', $this->img3])
            ->andFilterWhere(['like', 'img4', $this->img4]);

        return $dataProvider;
    }
}
