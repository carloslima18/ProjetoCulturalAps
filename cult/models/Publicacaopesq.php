<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "publicacaopesq".
 *
 * @property int $id
 * @property string $nome
 * @property string $redesocial
 * @property string $endereco
 * @property string $contato
 * @property string $email
 * @property string $atvexercida
 * @property string $categoria
 * @property string $anoinicio
 * @property string $cnpj
 * @property string $representacao
 * @property string $recurso
 * @property string $aprovado
 * @property double $latitude
 * @property double $longitude
 * @property string $geo_gps
 * @property int $pesquisador
 * @property string $img1
 * @property string $img2
 * @property string $img3
 * @property string $img4
 * @property string $campo1
 * @property string $campo2
 * @property string $campo3
 * @property string $campo4
 * @property string $campo5
 *
 * @property Avaliacaopubpesq[] $avaliacaopubpesqs
 * @property Pesquisador $pesquisador0
 */
class Publicacaopesq extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'publicacaopesq';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nome', 'endereco', 'contato', 'atvexercida', 'categoria'], 'required'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'anoinicio', 'cnpj', 'representacao', 'recurso', 'aprovado', 'img1', 'img2', 'img3', 'img4', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5'], 'string'],
            [['latitude', 'longitude'], 'number'],
            [['pesquisador'], 'default', 'value' => null],
            [['pesquisador'], 'integer'],
            [['pesquisador'], 'exist', 'skipOnError' => true, 'targetClass' => Pesquisador::className(), 'targetAttribute' => ['pesquisador' => 'id']],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nome' => 'Nome',
            'redesocial' => 'Rede social',
            'endereco' => 'Endereço',
            'contato' => 'Contato/cell',
            'email' => 'Email',
            'atvexercida' => 'Atv. exercida',
            'categoria' => 'Categoria',
            'anoinicio' => 'Ano de inicio',
            'cnpj' => 'Cnpj',
            'representacao' => 'Representacao',
            'recurso' => 'Recurso',
            'aprovado' => 'Aprovar',
            'latitude' => 'Latitude',
            'longitude' => 'Longitude',
            'geo_gps' => 'Geo Gps',
            'pesquisador' => 'Pesquisador',
            'img1' => 'Img1',
            'img2' => 'Img2',
            'img3' => 'Img3',
            'img4' => 'Img4',
            'campo1' => 'Campo1',
            'campo2' => 'Campo2',
            'campo3' => 'Campo3',
            'campo4' => 'Campo4',
            'campo5' => 'Campo5',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getAvaliacaopubpesqs()
    {
        return $this->hasMany(Avaliacaopubpesq::className(), ['idpubpesq' => 'id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPesquisador0()
    {
        return $this->hasOne(Pesquisador::className(), ['id' => 'pesquisador']);
    }
}
